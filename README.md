# Nova Intelligence

**Nova** es una aplicación Android de asistencia general, diseñada para crecer como la inteligencia central del ecosistema de aplicaciones.

> Una inteligencia, múltiples experiencias.

## Identidad

- Nombre: Nova Intelligence
- Nombre corto: Nova
- Plataforma inicial: Android
- Interfaz: Kotlin + Jetpack Compose
- Diseño: mezcla de estética cósmica, minimalismo y Liquid Glass
- Inteligencia: arquitectura multi-modelo, con proveedores intercambiables

## Estado actual — 0.2.0

Esta versión incorpora la primera interfaz de conversación y un cliente para conectarse con un motor de IA externo mediante Hugging Face Spaces.

- Chat nativo con burbujas de mensaje.
- Configuración de una URL de Nova Core.
- Solicitudes al endpoint de Gradio y recepción de respuestas.
- Backend de referencia en `nova-core/`, basado en Qwen3-1.7B y Hugging Face ZeroGPU.

**Importante:** el APK puede compilarse sin credenciales, pero las respuestas reales requieren que el Space de Nova Core esté creado, activo y disponible. Sin esa URL, la app explica cómo configurar la conexión y no inventa respuestas.

## Tecnologías

- Android Gradle Plugin: 9.5.0
- Gradle: 9.6.0
- Kotlin: 2.4.10
- Jetpack Compose BOM: 2026.09.00
- Activity Compose: 1.13.0
- SDK objetivo: 36
- SDK de compilación: 37 (preview)
- SDK mínimo: 26
- Java/JVM: 17

## Estructura

```text
Nova-Intelligence/
├── app/
│   └── src/main/
│       ├── java/com/jhooni467/novaintelligence/
│       │   ├── MainActivity.kt
│       │   ├── NovaSpaceClient.kt
│       │   └── ui/theme/NovaTheme.kt
│       └── res/
├── nova-core/
│   ├── app.py
│   ├── requirements.txt
│   └── README.md
├── docs/
│   ├── ROADMAP.md
│   └── AI_MVP_PLAN.md
├── gradle/
├── .github/workflows/android.yml
└── README.md
```

## Cómo activar la IA

El backend de referencia usa Hugging Face ZeroGPU y el modelo abierto Qwen3-1.7B. Consulta [nova-core/README.md](nova-core/README.md) y [docs/AI_MVP_PLAN.md](docs/AI_MVP_PLAN.md) para los pasos y límites actuales.

La cuota de ZeroGPU es limitada y puede haber cola o suspensión por inactividad. Las cuentas gratuitas que alojan su propio Space deben cumplir los requisitos actuales de Hugging Face, incluida antigüedad y verificación de cuenta. No se requiere una API key de pago ni se deben introducir tokens en la APK.

## Seguridad

- No incluir claves privadas de proveedores dentro de la APK o del repositorio.
- No introducir tokens en el código fuente.
- Validar las direcciones del backend y limitar el tamaño del historial.
- No enviar datos personales o confidenciales a un servicio externo.
- Cada integración debe declarar permisos y acciones disponibles.
- Nova debe pedir confirmación antes de acciones sensibles o difíciles de revertir.

## Hoja de ruta

Consulta [docs/ROADMAP.md](docs/ROADMAP.md) para conocer las próximas etapas.
