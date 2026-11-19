# Nova Intelligence — MVP de IA real (v0.2.0)

## 1. Objetivo observable

La aplicación Android debe enviar un mensaje desde su chat a un modelo de lenguaje real, recibir una respuesta generada por el modelo y mostrarla en una conversación nativa.

## 2. Fuente y estado verificado

- Repositorio: https://github.com/jhooni467/Nova-Intelligence
- Rama: `main`
- Base inicial revisada: commit `33e748347d2a6f4c2d722a77b9a184ea8af82687`.
- La APK 0.1.0 compila correctamente mediante GitHub Actions.
- La aplicación inicial no tenía conexión con ningún modelo.
- El desarrollador trabaja desde Android y GitHub, sin Android Studio ni PC.

## 3. Decisiones aceptadas para este MVP

- Plataforma: Android nativo, Kotlin y Jetpack Compose.
- Primer modelo: Qwen3-1.7B, alojado en un Hugging Face ZeroGPU Space.
- Transporte: API HTTP de Gradio documentada por Hugging Face.
- Historial inicial: memoria temporal en la pantalla; el cliente envía hasta 10 mensajes anteriores.
- No se integran todavía cuentas, autenticación, sincronización ni almacenamiento permanente del historial.
- No se incluyen claves de proveedores comerciales ni tokens dentro de la APK.
- La dirección del Space se configura en la propia aplicación y se guarda como preferencia no secreta.

## 4. Alcance incluido

- Chat nativo con burbujas de usuario y asistente.
- Campo de mensaje, sugerencias iniciales y estados de espera/error.
- Configuración de la URL del motor.
- Cliente HTTP para la API de Gradio.
- Backend de inferencia con Qwen3-1.7B y límites básicos de entrada.
- Instrucción de sistema para una experiencia respetuosa y apropiada para adolescentes.
- Compilación automática de APK mediante GitHub Actions.

## 5. Fuera de alcance

- Multi-modelo y selector de proveedor.
- Memoria persistente, sincronización o cuenta de usuario.
- Voz, imágenes, documentos y navegación web.
- Agentes y acciones sobre Pulsar RM o AstralWorld.
- Garantía de disponibilidad, tiempo de respuesta o cuota del servicio gratuito.

## 6. Trazabilidad

| Requisito | Componente | Tarea | Criterio de aceptación |
|---|---|---|---|
| R-01 Enviar mensajes | `MainActivity.kt` | Mostrar chat y gestionar estado | El usuario puede escribir y ver su mensaje |
| R-02 Conectar el modelo | `NovaSpaceClient.kt` | Integrar API Gradio en dos pasos | Se recibe y parsea una respuesta real del Space |
| R-03 Configurar motor | `ConnectionDialog` | Guardar URL HTTPS de `.hf.space` | Se rechazan URLs no válidas y se conserva la URL |
| R-04 Inferencia | `nova-core/app.py` | Cargar Qwen3 y generar texto | La API devuelve `{"reply":"..."}` |
| R-05 Seguridad básica | Cliente y backend | Limitar tamaño, historial y tiempos | Las entradas vacías/excesivas se rechazan; no se registra el contenido |
| R-06 APK reproducible | GitHub Actions | Compilar versión 0.2.0 | El workflow termina en success y publica el artefacto |

## 7. Restricciones y riesgos

- ZeroGPU requiere una cuenta verificada con más de 30 días de antigüedad para alojar un Space propio con cuenta gratuita.
- La cuota actual de ZeroGPU es limitada: 5 minutos diarios por cuenta gratuita autenticada y 2 minutos para solicitudes anónimas.
- Los Spaces públicos exponen el código y aceptan solicitudes públicas; no deben contener secretos.
- El modelo o el servicio pueden cambiar, dormir, entrar en cola o dejar de estar disponibles.
- La instrucción de sistema ayuda a orientar las respuestas, pero no garantiza por sí sola una moderación perfecta.
- La app debe mostrar los errores en vez de simular respuestas cuando el modelo no esté disponible.

## 8. Pruebas de aceptación

1. Abrir Nova sin URL configurada: la app debe seguir funcionando y explicar cómo configurar el motor.
2. Guardar una URL con dominio distinto de `.hf.space`: debe rechazarse.
3. Guardar una URL válida: debe conservarse tras cerrar y abrir la app.
4. Enviar un mensaje con Space activo: debe aparecer el mensaje de usuario y luego el texto devuelto por el modelo.
5. Enviar con Space dormido o no disponible: debe mostrarse un error recuperable, sin cerrar la app.
6. Enviar con cuota agotada: debe mostrarse un aviso de límite, sin inventar una respuesta.
7. Confirmar que no hay tokens/API keys en el APK ni en GitHub.
8. Confirmar que GitHub Actions compila y publica la APK 0.2.0.

## 9. Entrega y recuperación

- Revisar el resultado de GitHub Actions antes de instalar la APK.
- Mantener el endpoint configurable para poder reemplazar el Space o el modelo.
- Si el proveedor gratuito deja de estar disponible, conservar la interfaz y sustituir únicamente el adaptador remoto.
- No activar proveedores de pago ni introducir secretos sin autorización.

## 10. Completion ledger

| Elemento | Estado inicial antes de esta entrega | Evidencia necesaria |
|---|---|---|
| Diseño del MVP | Reviewed | Este documento |
| Chat Compose | Attempted | APK instalada y navegación manual |
| Cliente Gradio | Attempted | Respuesta real del Space |
| Backend ZeroGPU | Draft/Attempted | Space compilado y endpoint probado |
| APK 0.2.0 | Unknown | Workflow GitHub Actions en success |
| IA real dentro de la app | Unknown | Intercambio real mensaje/respuesta |

**Siguiente acción concreta:** crear o utilizar una cuenta Hugging Face elegible, publicar el Space gratuito y pegar su dirección en Nova.
