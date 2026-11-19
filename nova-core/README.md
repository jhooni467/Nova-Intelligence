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

## Archivos requeridos

Los tres archivos de esta carpeta deben quedar directamente en la raíz del Space:

- **app.py**
- **requirements.txt**
- **README.md**

El Space debe usar el SDK **Gradio** y el hardware **ZeroGPU**. La implementación sigue el patrón recomendado por Hugging Face: cargar los pesos en CUDA a nivel de módulo y ejecutar la inferencia dentro de una función decorada con `@spaces.GPU`.

## Configuración y publicación

1. Crea un Space nuevo en Hugging Face.
2. Selecciona **Gradio** como SDK y **ZeroGPU** como hardware.
3. Para la primera integración, configúralo como **Public**: el cliente Android todavía no envía autenticación.
4. Sube los tres archivos de esta carpeta a la raíz del Space.
5. Espera a que termine la construcción y la carga del modelo.
6. Copia la dirección de la aplicación que termina en **.hf.space** y agrégala en la configuración de Nova.

Guía paso a paso desde Android: [docs/NOVA_CORE_DEPLOYMENT.md](../docs/NOVA_CORE_DEPLOYMENT.md).

## Requisitos de la cuenta y límites gratuitos

- Las cuentas personales gratuitas en buen estado, con correo verificado y más de 30 días de antigüedad, pueden alojar hasta 2 Spaces ZeroGPU.
- La cuota diaria documentada es de 5 minutos para una cuenta gratuita autenticada y 2 minutos para solicitudes no autenticadas.
- La app Android actual no envía un token de Hugging Face; por tanto, las solicitudes de Nova deben tratarse como no autenticadas y pueden quedar limitadas a 2 minutos diarios.
- ZeroGPU usa GPU compartida y puede tener colas, arranques en frío y pausas.
- Las condiciones y cuotas pueden cambiar. Consulta la [documentación oficial de ZeroGPU](https://huggingface.co/docs/hub/spaces-zerogpu).

## API que consume Nova

- Método: **POST**
- Ruta: **/gradio_api/call/chat**
- Entrada: un objeto JSON con **message** y **history**.
- Salida: un objeto JSON con **reply**.
- Gradio devuelve un identificador de evento y después el resultado mediante Server-Sent Events (SSE).

La aplicación Android realiza las dos solicitudes automáticamente.

## Privacidad y seguridad

- No se guarda el historial de conversación de forma permanente en el código de este servidor.
- El historial se envía desde el dispositivo solo para dar contexto a la conversación.
- No añadas tokens ni claves al repositorio ni a la APK.
- El Space es público en esta configuración: cualquiera con su dirección puede invocar la API y el código fuente es visible.
- No uses Nova Core para enviar contraseñas, datos bancarios, documentos de identidad u otra información confidencial.

Este Space es un prototipo personal, no un servicio de producción ni una API con disponibilidad garantizada.
