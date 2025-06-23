package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.EstudianteDTO;
import com.hitss.spring.apiacademicplatform.dto.NotaDTO;
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

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private NotaService notaService;

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> getAllEstudiantes() {
        List<EstudianteDTO> estudiantes = estudianteService.getAllEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> getEstudianteById(@PathVariable Long id) {
        return estudianteService.getEstudianteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstudianteDTO> createEstudiante(@Valid @RequestBody EstudianteDTO estudianteDTO, BindingResult result) {
        handleValidationErrors(result);
        EstudianteDTO createdEstudiante = estudianteService.createEstudiante(estudianteDTO);
        return new ResponseEntity<>(createdEstudiante, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> updateEstudiante(@PathVariable Long id, @Valid @RequestBody EstudianteDTO estudianteDTO, BindingResult result) {
        handleValidationErrors(result);
        EstudianteDTO updatedEstudiante = estudianteService.updateEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(updatedEstudiante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable Long id) {
        estudianteService.deleteEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/notas")
    public ResponseEntity<List<NotaDTO>> getNotasByEstudiante(@PathVariable Long id) {
        List<NotaDTO> notas = notaService.getNotasByEstudianteId(id);
        if (notas.isEmpty() && !estudianteService.getEstudianteById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(notas);
    }
}
