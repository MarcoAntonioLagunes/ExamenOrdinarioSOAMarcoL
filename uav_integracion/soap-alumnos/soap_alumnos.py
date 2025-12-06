from spyne import Application, rpc, ServiceBase, Unicode, Integer
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication

# "Base de datos" en memoria solo para la demo
alumnos = {}

class AlumnoService(ServiceBase):

    @rpc(Unicode, Unicode, Integer, Unicode, Unicode, _returns=Unicode)
    def registrar_alumno(ctx, matricula, nombre, edad, correo, telefono):
        alumnos[matricula] = {
            "matricula": matricula,
            "nombre": nombre,
            "edad": edad,
            "correo": correo,
            "telefono": telefono,
        }
        return "Alumno registrado correctamente"

    @rpc(Unicode, _returns=Unicode)
    def buscar_alumno(ctx, matricula):
        alumno = alumnos.get(matricula)
        if not alumno:
            return "NO_ENCONTRADO"

        # Devolvemos una cadena sencilla separada por |
        return f'{alumno["matricula"]}|{alumno["nombre"]}|{alumno["edad"]}|{alumno["correo"]}|{alumno["telefono"]}'

    @rpc(Unicode, _returns=Unicode)
    def eliminar_alumno(ctx, matricula):
        # Elimina un alumno por matrícula; devuelve mensaje o NO_ENCONTRADO
        if matricula in alumnos:
            del alumnos[matricula]
            return "ALUMNO_ELIMINADO"
        return "NO_ENCONTRADO"


# Configuración de la app SOAP
soap_app = Application(
    [AlumnoService],
    "mx.uav.alumnos",
    in_protocol=Soap11(validator="lxml"),
    out_protocol=Soap11()
)

wsgi_app = WsgiApplication(soap_app)


def app_with_cors(environ, start_response):
    """Envuelve la app SOAP para permitir CORS desde el frontend."""
    method = environ.get("REQUEST_METHOD", "GET").upper()

    # Pre-flight (OPTIONS)
    if method == "OPTIONS":
        headers = [
            ("Access-Control-Allow-Origin", "*"),
            ("Access-Control-Allow-Methods", "POST, OPTIONS"),
            ("Access-Control-Allow-Headers", "Content-Type, SOAPAction"),
            ("Access-Control-Max-Age", "3600"),
        ]
        start_response("200 OK", headers)
        return [b""]

    def custom_start_response(status, headers, exc_info=None):
        headers.append(("Access-Control-Allow-Origin", "*"))
        return start_response(status, headers, exc_info)

    return wsgi_app(environ, custom_start_response)


if __name__ == "__main__":
    from wsgiref.simple_server import make_server

    server = make_server("0.0.0.0", 8000, app_with_cors)
    print("SOAP AlumnoService escuchando en http://localhost:8000/soap")
    server.serve_forever()
