# Nova Intelligence — Hoja de ruta

## Objetivo

Crear una aplicación Android de asistencia general llamada **Nova**, con identidad propia y capacidad de conectarse con las aplicaciones del ecosistema.

## Versiones

### 0.1.0 — Base visual
- Estructura Android con Kotlin y Jetpack Compose.
- Tema cósmico oscuro e identidad inicial.
- Pantalla de bienvenida.

### 0.2.0 — Primer chat e integración de IA
- Chat nativo con mensajes de usuario y asistente.
- Estados de carga y error.
- Configuración del endpoint de Nova Core.
- Cliente de Gradio para comunicarse con un modelo real.
- Backend de referencia con Qwen3-1.7B en Hugging Face ZeroGPU.
- Límites de entrada e historial para el primer MVP.

**Estado:** código preparado en el repositorio; la conexión real depende de publicar y activar el Space de Nova Core.

### 0.3.0 — Conversaciones
- Guardar historial local con Room.
- Crear, renombrar y eliminar conversaciones.
- Recuperar conversaciones al reiniciar la app.
- Mejoras de errores, reintentos y respuesta en streaming.

### 0.4.0 — Nova Core ampliado
- Contrato común para proveedores.
- Selector de modelo y proveedor.
- Límites por proveedor y respuestas de disponibilidad.
- Configuración segura de autenticación.

### 0.5.0 — Personalización y memoria
- Preferencias editables.
- Recuerdos bajo control del usuario.
- Ver, editar, pausar y borrar recuerdos.

### 0.6.0 — Herramientas
- Búsqueda web con fuentes visibles.
- Análisis de documentos e imágenes, según el modelo.
- Herramientas y acciones con confirmación.

### 0.7.0 — Voz
- Entrada por voz.
- Respuestas habladas.
- Modo de conversación, sujeto al proveedor disponible.

### 0.8.0 — Nova Connect
- Sistema de permisos y conexiones.
- Contratos de integración.
- Registro de acciones y confirmación antes de ejecutar cambios importantes.

### 0.9.0 — Ecosistema
- Integración con Pulsar RM.
- Integración con AstralWorld.
- Aplicaciones futuras.

### 1.0.0 — Primera versión estable
- Estabilidad, privacidad, accesibilidad y pruebas de compatibilidad.
- Documentación y flujo de actualización.

## Reglas de seguridad

- No incluir claves privadas de proveedores dentro del APK o del repositorio.
- Las llamadas a modelos externos deben usar conexiones protegidas.
- No enviar información personal sensible innecesaria.
- Cada integración debe declarar permisos y acciones disponibles.
- Nova debe pedir confirmación antes de acciones sensibles o difíciles de revertir.
