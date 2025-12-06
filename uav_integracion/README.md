# Proyecto Integración UAV (SOAP + REST + Frontend)

Estructura:

- `soap-alumnos/`  -> Servicio SOAP en Python para Matriculas (Alumnos).
- `curso-rest/`    -> API REST de Cursos en Java Spring Boot.
- `frontend/`      -> Mini frontend HTML/JS que consume ambos servicios.

Pasos generales:

1. Configurar base de datos MySQL `uav` con tablas `alumnos` y `cursos`.
2. Levantar servicio SOAP de alumnos.
3. Levantar servicio REST de cursos.
4. Abrir el `frontend/index.html` y probar registros y operaciones.
