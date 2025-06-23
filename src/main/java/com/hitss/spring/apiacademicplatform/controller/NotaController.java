package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.NotaDTO;
import com.hitss.spring.apiacademicplatform.model.Nota;
import com.hitss.spring.apiacademicplatform.service.AsignaturaService;
import com.hitss.spring.apiacademicplatform.service.EstudianteService;
import com.hitss.spring.apiacademicplatform.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;
    @Autowired
    private AsignaturaService asignaturaService;
    @Autowired
    private EstudianteService estudianteService;

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<NotaDTO>> getAllNotas() {
        List<NotaDTO> notas = notaService.getAllNotas();
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> getNotaById(@PathVariable Long id) {
        return notaService.getNotaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> crearNota(@Valid @RequestBody NotaDTO notaDTO) {
        try {
            Nota nuevaNota = notaService.createNota(notaDTO);
            return new ResponseEntity<>(nuevaNota, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la nota: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> updateNota(@PathVariable Long id, @Valid @RequestBody NotaDTO notaDTO, BindingResult result) {
        handleValidationErrors(result);
        NotaDTO updatedNota = notaService.updateNota(id, notaDTO);
        return ResponseEntity.ok(updatedNota);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNota(@PathVariable Long id) {
        notaService.deleteNota(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/asignatura/{asignaturaId}")
    public ResponseEntity<List<NotaDTO>> getNotasByAsignatura(@PathVariable Long asignaturaId) {
        List<NotaDTO> notas = notaService.getNotasByAsignaturaId(asignaturaId);
        if (notas.isEmpty() && !asignaturaService.getAsignaturaById(asignaturaId).isPresent()) { // Necesitas inyectar AsignaturaService
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + asignaturaId);
        }
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<NotaDTO>> getNotasByEstudiante(@PathVariable Long estudianteId) {
        List<NotaDTO> notas = notaService.getNotasByEstudianteId(estudianteId);
        if (notas.isEmpty() && !estudianteService.getEstudianteById(estudianteId).isPresent()) { // Necesitas inyectar EstudianteService
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + estudianteId);
        }
        return ResponseEntity.ok(notas);
    }
}
