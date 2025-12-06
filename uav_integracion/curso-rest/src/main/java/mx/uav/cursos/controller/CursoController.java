package mx.uav.cursos.controller;

import mx.uav.cursos.model.Curso;
import mx.uav.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    // ---------- DTO para recibir datos del frontend ----------
    public static class CursoDto {
        private String nombre;
        private String fechaInicio; // viene como texto yyyy-MM-dd
        private String profesor;

        public CursoDto() {
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(String fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public String getProfesor() {
            return profesor;
        }

        public void setProfesor(String profesor) {
            this.profesor = profesor;
        }
    }

    // ---------- Ping ----------
    @GetMapping("/ping")
    public String ping() {
        return "API Cursos OK";
    }

    // ---------- Registrar curso ----------
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody CursoDto dto) {
        try {
            if (dto.getNombre() == null || dto.getNombre().isBlank()
                    || dto.getFechaInicio() == null || dto.getFechaInicio().isBlank()
                    || dto.getProfesor() == null || dto.getProfesor().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "Todos los campos son obligatorios."));
            }

            Curso curso = new Curso();
            curso.setNombre(dto.getNombre().trim());
            curso.setProfesor(dto.getProfesor().trim());

            // Formato esperado: yyyy-MM-dd
            LocalDate fecha = LocalDate.parse(dto.getFechaInicio().trim());
            curso.setFechaInicio(fecha);

            cursoRepository.save(curso);

            return ResponseEntity.ok(Map.of("mensaje", "Curso registrado correctamente."));
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "mensaje", "Formato de fecha inválido. Usa yyyy-MM-dd.",
                            "error", e.getMessage()
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al registrar curso.",
                            "error", e.getMessage()
                    ));
        }
    }

    // ---------- Buscar curso ----------
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String nombre) {
        return cursoRepository.findByNombre(nombre)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "Curso no encontrado.")));
    }

    // ---------- Editar curso ----------
    @PutMapping("/editar")
    public ResponseEntity<?> editar(@RequestBody CursoDto dto) {
        try {
            if (dto.getNombre() == null || dto.getNombre().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El nombre del curso es obligatorio para editar."));
            }

            return cursoRepository.findByNombre(dto.getNombre().trim())
                    .map(curso -> {
                        if (dto.getProfesor() != null && !dto.getProfesor().isBlank()) {
                            curso.setProfesor(dto.getProfesor().trim());
                        }
                        if (dto.getFechaInicio() != null && !dto.getFechaInicio().isBlank()) {
                            curso.setFechaInicio(LocalDate.parse(dto.getFechaInicio().trim()));
                        }
                        cursoRepository.save(curso);
                        return ResponseEntity.ok(Map.of("mensaje", "Curso actualizado correctamente."));
                    })
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("mensaje", "Curso no encontrado.")));

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "mensaje", "Formato de fecha inválido. Usa yyyy-MM-dd.",
                            "error", e.getMessage()
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al actualizar curso.",
                            "error", e.getMessage()
                    ));
        }
    }
}
