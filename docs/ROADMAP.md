# Nova Intelligence — Hoja de ruta inicial

## Objetivo

Crear una aplicación Android de asistencia general llamada **Nova**, con una identidad propia y capacidad de conectarse con las aplicaciones del ecosistema.

## Etapas propuestas

### 0. Base del proyecto
- Repositorio y estructura Android.
- Kotlin y Jetpack Compose.
- Identidad visual inicial.
- Configuración de compilación.

### 1. Interfaz y navegación
- Inicio y conversación.
- Historial.
- Explorar.
- Configuración.
- Estados de carga, error y conexión.

### 2. Nova Core
- Capa central de coordinación.
- Contrato común para proveedores de modelos.
- Conexión segura a un backend.
- Gestión de errores y límites.

### 3. Sistema multi-modelo
- Proveedores intercambiables.
- Modelo predeterminado.
- Selección manual.
- Enrutamiento por tipo de tarea.

### 4. Historial y memoria
- Conversaciones guardadas.
- Preferencias controladas por el usuario.
- Ver, editar y borrar recuerdos.
- Almacenamiento protegido.

### 5. Voz
- Entrada por voz.
- Respuestas habladas.
- Modo de conversación en tiempo real, según el proveedor disponible.

### 6. Nova Connect
- Sistema de permisos y conexiones.
- Contratos de integración.
- Confirmación antes de ejecutar acciones importantes.

### 7. Integraciones del ecosistema
- Pulsar RM.
- AstralWorld.
- Aplicaciones futuras.

## Alcance de la versión 0.1.0

La primera base contiene el proyecto Android y una pantalla inicial de presentación. Todavía no incluye un modelo conectado, backend, memoria, voz ni integraciones.

## Reglas de seguridad

- No incluir claves privadas de proveedores dentro del APK o del repositorio.
- Las llamadas a modelos deben pasar por un backend protegido.
- Cada integración debe declarar permisos y acciones disponibles.
- Nova debe pedir confirmación antes de acciones sensibles o difíciles de revertir.
