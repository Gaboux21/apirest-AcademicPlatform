package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.AsignaturaDTO;
import com.hitss.spring.apiacademicplatform.service.AsignaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {

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
    public ResponseEntity<List<AsignaturaDTO>> getAllAsignaturas() {
        List<AsignaturaDTO> asignaturas = asignaturaService.getAllAsignaturas();
        return ResponseEntity.ok(asignaturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> getAsignaturaById(@PathVariable Long id) {
        return asignaturaService.getAsignaturaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AsignaturaDTO> createAsignatura(@Valid @RequestBody AsignaturaDTO asignaturaDTO, BindingResult result) {
        handleValidationErrors(result);
        AsignaturaDTO createdAsignatura = asignaturaService.createAsignatura(asignaturaDTO);
        return new ResponseEntity<>(createdAsignatura, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> updateAsignatura(@PathVariable Long id, @Valid @RequestBody AsignaturaDTO asignaturaDTO, BindingResult result) {
        handleValidationErrors(result);
        AsignaturaDTO updatedAsignatura = asignaturaService.updateAsignatura(id, asignaturaDTO);
        return ResponseEntity.ok(updatedAsignatura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsignatura(@PathVariable Long id) {
        asignaturaService.deleteAsignatura(id);
        return ResponseEntity.noContent().build();
    }
}
