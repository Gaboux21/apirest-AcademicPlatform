package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.AsignaturaDTO;
import com.hitss.spring.apiacademicplatform.dto.ProfesorDTO;
import com.hitss.spring.apiacademicplatform.service.AsignaturaService;
import com.hitss.spring.apiacademicplatform.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;
    @Autowired
    private AsignaturaService asignaturaService;

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProfesorDTO>> getAllProfesores() {
        List<ProfesorDTO> profesores = profesorService.getAllProfesores();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> getProfesorById(@PathVariable Long id) {
        return profesorService.getProfesorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfesorDTO> createProfesor(@Valid @RequestBody ProfesorDTO profesorDTO, BindingResult result) {
        handleValidationErrors(result);
        ProfesorDTO createdProfesor = profesorService.createProfesor(profesorDTO);
        return new ResponseEntity<>(createdProfesor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDTO> updateProfesor(@PathVariable Long id, @Valid @RequestBody ProfesorDTO profesorDTO, BindingResult result) {
        handleValidationErrors(result);
        ProfesorDTO updatedProfesor = profesorService.updateProfesor(id, profesorDTO);
        return ResponseEntity.ok(updatedProfesor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable Long id) {
        profesorService.deleteProfesor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/asignaturas")
    public ResponseEntity<List<AsignaturaDTO>> getAsignaturasByProfesor(@PathVariable Long id) {
        List<AsignaturaDTO> asignaturas = asignaturaService.getAsignaturasByProfesorId(id);
        if (asignaturas.isEmpty() && !profesorService.getProfesorById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(asignaturas);
    }
}
