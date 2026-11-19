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

## Estado actual — 0.1.0

Esta versión establece la base del proyecto:

- Estructura inicial de Android.
- Tema oscuro y paleta visual de Nova.
- Pantalla inicial con campo de texto y sugerencias.
- Identificador de aplicación: `com.jhooni467.novaintelligence`.

**Importante:** la pantalla actual es una primera interfaz. Nova Core todavía no está conectado a ningún modelo de IA; por eso el campo de conversación aún no envía mensajes reales.

## Tecnologías

- Android Gradle Plugin: 9.5.0
- Gradle: 9.6.0
- Kotlin: 2.4.10
- Jetpack Compose BOM: 2026.09.00
- Activity Compose: 1.13.0
- SDK objetivo y de compilación: 36
- SDK mínimo: 26
- Java/JVM: 17

## Estructura

```text
Nova-Intelligence/
├── app/
│   └── src/main/
│       ├── java/com/jhooni467/novaintelligence/
│       │   ├── MainActivity.kt
│       │   └── ui/theme/NovaTheme.kt
│       └── res/
├── docs/
│   └── ROADMAP.md
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Evolución prevista

1. Interfaz y navegación.
2. Nova Core y backend seguro.
3. Sistema multi-modelo.
4. Historial y memoria.
5. Voz.
6. Nova Connect.
7. Integración con Pulsar RM y AstralWorld.

## Seguridad

Las claves de los proveedores de IA no deben guardarse en la aplicación Android ni subirse al repositorio. Las integraciones se realizarán mediante servicios protegidos y permisos explícitos.

## Documentación

Consulta [docs/ROADMAP.md](docs/ROADMAP.md) para conocer las etapas de desarrollo.
