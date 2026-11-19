---
title: Nova Core
emoji: "✨"
colorFrom: purple
colorTo: indigo
sdk: gradio
sdk_version: 5.49.1
python_version: "3.10"
app_file: app.py
pinned: false
license: apache-2.0
---

# Nova Core

Motor de conversación para Nova Intelligence.

## Modelo

Este Space utiliza [Qwen3-1.7B](https://huggingface.co/Qwen/Qwen3-1.7B), un modelo abierto publicado bajo Apache 2.0. El modelo se ejecuta en Hugging Face ZeroGPU; no se necesita una API key de un proveedor comercial.

## Configuración

1. Crea un Space con SDK **Gradio**.
2. Selecciona **ZeroGPU** como hardware.
3. Coloca estos archivos en la raíz del Space: `README.md`, `app.py` y `requirements.txt`.
4. Espera a que termine la construcción y la carga del modelo.
5. Copia la dirección que termina en `.hf.space` y agrégala en la configuración de Nova.

## Límites gratuitos

ZeroGPU ofrece cuotas diarias y colas compartidas. La documentación actual indica 5 minutos diarios de GPU para una cuenta gratuita autenticada y 2 minutos para solicitudes sin autenticar. El alojamiento de un ZeroGPU Space propio requiere una cuenta gratuita verificada y con más de 30 días de antigüedad. Las cuotas, la disponibilidad y las condiciones pueden cambiar.

## API que consume Nova

- Método: `POST`
- Ruta: `/gradio_api/call/chat`
- Entrada: un objeto JSON con `message` y `history`.
- Salida: un objeto JSON con `reply`.

Gradio utiliza un identificador de evento para consultar el resultado final. La aplicación Android realiza las dos solicitudes automáticamente.

## Privacidad y seguridad

- No se guarda el historial en el proceso servidor.
- El historial se envía desde el dispositivo solo para dar contexto a la conversación.
- No añadas tokens ni claves al repositorio.
- El Space es público cuando se publica como público: cualquier persona con su dirección puede invocar la API y el código fuente es visible.
- No uses Nova Core para enviar contraseñas, datos bancarios, documentos de identidad u otra información confidencial.

Este Space es un prototipo personal, no un servicio de producción ni una API con disponibilidad garantizada.
