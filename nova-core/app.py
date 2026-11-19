import gradio as gr
import spaces
import torch
from transformers import AutoModelForCausalLM, AutoTokenizer

MODEL_ID = "Qwen/Qwen3-1.7B"

SYSTEM_PROMPT = """Eres Nova, un asistente de inteligencia artificial útil, natural y respetuoso.
Responde en el idioma del usuario; si escribe en español, responde en español.
Habla con claridad y naturalidad. No comiences cada respuesta presentándote ni repitas tu nombre.
Adapta la extensión a la pregunta y reconoce cuando no sabes algo. No afirmes haber buscado en internet ni haber ejecutado acciones si no lo hiciste.
Protege la privacidad: no solicites contraseñas, direcciones exactas, documentos de identidad ni datos personales innecesarios.
Mantén una experiencia apropiada para adolescentes. No generes contenido sexual explícito ni facilites autolesiones, violencia real, armas, sustancias peligrosas, retos peligrosos o actividades ilegales. Ante una situación de riesgo, responde con apoyo y recomienda acudir a una persona adulta de confianza o a servicios locales de emergencia, sin ofrecer instrucciones dañinas.
No afirmes tener conciencia, sentimientos humanos ni una relación personal con el usuario."""

# ZeroGPU requiere cargar el modelo en el dispositivo CUDA a nivel de módulo.
tokenizer = AutoTokenizer.from_pretrained(MODEL_ID)
model = AutoModelForCausalLM.from_pretrained(
    MODEL_ID,
    torch_dtype=torch.bfloat16,
    device_map="cuda",
)
model.eval()


@spaces.GPU(duration=60)
def chat(payload):
    if not isinstance(payload, dict):
        raise gr.Error("La solicitud no tiene un formato válido.")

    message = payload.get("message", "")
    if not isinstance(message, str):
        raise gr.Error("El mensaje debe ser texto.")

    message = message.strip()
    if not message:
        raise gr.Error("Escribe un mensaje antes de enviarlo.")
    if len(message) > 2000:
        raise gr.Error("El mensaje supera el límite de 2,000 caracteres.")

    raw_history = payload.get("history", [])
    if not isinstance(raw_history, list):
        raw_history = []

    messages = [{"role": "system", "content": SYSTEM_PROMPT}]

    for turn in raw_history[-10:]:
        if not isinstance(turn, dict):
            continue

        role = turn.get("role")
        content = turn.get("content")

        if role not in ("user", "assistant") or not isinstance(content, str):
            continue

        content = content.strip()
        if content:
            messages.append({
                "role": role,
                "content": content[:1500],
            })

    messages.append({"role": "user", "content": message})

    try:
        rendered_prompt = tokenizer.apply_chat_template(
            messages,
            tokenize=False,
            add_generation_prompt=True,
            enable_thinking=False,
        )

        model_inputs = tokenizer(
            rendered_prompt,
            return_tensors="pt",
            truncation=True,
            max_length=8192,
        ).to(model.device)

        with torch.inference_mode():
            generated = model.generate(
                **model_inputs,
                max_new_tokens=256,
                do_sample=True,
                temperature=0.7,
                top_p=0.8,
                top_k=20,
                repetition_penalty=1.05,
                pad_token_id=tokenizer.eos_token_id,
                eos_token_id=tokenizer.eos_token_id,
            )

        new_tokens = generated[0][model_inputs.input_ids.shape[-1]:]
        answer = tokenizer.decode(
            new_tokens,
            skip_special_tokens=True,
        ).strip()

        if not answer:
            raise gr.Error("El modelo no produjo una respuesta. Inténtalo de nuevo.")

        return {"reply": answer}

    except gr.Error:
        raise
    except torch.cuda.OutOfMemoryError:
        raise gr.Error("Nova Core se quedó sin memoria temporal. Inténtalo más tarde.")
    except Exception:
        # No registramos ni devolvemos el contenido privado de los mensajes.
        raise gr.Error("Ocurrió un error al generar la respuesta. Inténtalo de nuevo.")


demo = gr.Interface(
    fn=chat,
    inputs=gr.JSON(label="Solicitud"),
    outputs=gr.JSON(label="Respuesta"),
    title="Nova Core",
    description="Motor de conversación de Nova Intelligence. Las solicitudes se procesan al recibirlas; no se guarda el historial en este servidor.",
    api_name="chat",
    allow_flagging="never",
)

demo.queue(default_concurrency_limit=1)

if __name__ == "__main__":
    demo.launch()
