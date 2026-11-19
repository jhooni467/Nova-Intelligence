# Publicar Nova Core desde un teléfono Android

Esta guía sirve para publicar el backend que ya está preparado en la carpeta **nova-core/** del repositorio de Nova Intelligence.

## Antes de empezar

- Se necesita una cuenta de Hugging Face en buen estado, con correo verificado y más de 30 días de antigüedad para alojar un Space propio con ZeroGPU gratuito.
- Una cuenta personal gratuita elegible puede alojar hasta dos Spaces ZeroGPU.
- No necesitas contratar un plan de pago para este prototipo.
- La aplicación Android actual no envía un token de Hugging Face. Por eso sus peticiones llegan como no autenticadas y deben contar con la cuota anónima de ZeroGPU: actualmente 2 minutos diarios. Las solicitudes autenticadas de una cuenta gratuita tienen una cuota de 5 minutos diarios.
- ZeroGPU usa recursos compartidos: puede haber cola, arranques en frío o pausas temporales.

Consulta los requisitos vigentes antes de crear el Space: https://huggingface.co/docs/hub/spaces-zerogpu

## 1. Crear el Space

1. Inicia sesión en Hugging Face.
2. Abre la sección **Spaces** y elige **Create new Space**.
3. Ponle un nombre como **nova-core**.
4. Selecciona **Gradio** como SDK.
5. Selecciona **ZeroGPU** como hardware.
6. Para esta primera prueba, el Space debe ser **Public**, porque Nova todavía no tiene autenticación. Un Space público permite que otras personas accedan a su aplicación y vean su código.
7. Crea el Space.

Si la cuenta todavía no cumple los 30 días, puedes preparar los archivos en GitHub, pero Hugging Face no permitirá alojar tu propio Space ZeroGPU gratuito hasta que cumpla ese requisito. No cambies datos de la cuenta ni intentes saltarte las verificaciones.

## 2. Subir los tres archivos correctos

En el repositorio de Nova Intelligence, abre la carpeta **nova-core/**.

Sube únicamente estos tres archivos a la **raíz del Space**:

- **app.py**
- **requirements.txt**
- **README.md**

No subas la carpeta completa de Android ni coloques estos archivos dentro de otra subcarpeta. Hugging Face debe encontrar app.py y requirements.txt en el nivel principal del Space.

## 3. Esperar a que inicie

Después de subir los archivos:

1. Abre la pestaña **Logs** del Space si la compilación falla.
2. Espera a que el estado cambie a **Running**.
3. En la aplicación Gradio aparecerá una entrada JSON. Para una prueba manual, usa:

~~~json
{
  "message": "Hola, Nova",
  "history": []
}
~~~

La respuesta debe tener la forma:

~~~json
{
  "reply": "..."
}
~~~

La primera carga puede tardar porque el modelo Qwen3-1.7B debe descargarse y prepararse.

## 4. Conectar Nova Intelligence

Usa la dirección de la aplicación Gradio, que termina en **.hf.space**, por ejemplo:

~~~text
https://TU-USUARIO-nova-core.hf.space
~~~

No uses la dirección de la página de administración huggingface.co/spaces/....

En Nova Intelligence:

1. Abre **Conectar IA** o **Configurar motor de IA**.
2. Pega la dirección .hf.space.
3. Guarda la conexión.
4. Envía un mensaje sencillo, por ejemplo: “Hola, ¿quién eres?”

La app comprobará la conexión al enviar el primer mensaje.

## 5. Privacidad y límites

- El Space público expone el código y su endpoint puede ser utilizado por otras personas.
- No envíes contraseñas, información bancaria, documentos ni datos privados.
- No pongas tokens ni claves en GitHub, en app.py, ni dentro del APK.
- El historial se envía desde el teléfono para dar contexto a la conversación; este prototipo no implementa almacenamiento permanente de conversaciones.
- La instrucción de sistema ayuda a orientar las respuestas, pero no garantiza que el modelo siempre responda correctamente.
- La cuota anónima actual es de 2 minutos diarios para las solicitudes que hace la app sin iniciar sesión en Hugging Face. No debe considerarse un servicio ilimitado.

## Estado

Los archivos base ya están en **nova-core/**. La publicación en Hugging Face y la primera respuesta real todavía requieren completar estos pasos desde la cuenta del propietario del Space.
