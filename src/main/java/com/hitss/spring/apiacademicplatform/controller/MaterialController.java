package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.MaterialDTO;
import com.hitss.spring.apiacademicplatform.service.AsignaturaService;
import com.hitss.spring.apiacademicplatform.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private AsignaturaService asignaturaService; // Inyectar para validación

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<MaterialDTO>> getAllMateriales() {
        List<MaterialDTO> materiales = materialService.getAllMateriales();
        return ResponseEntity.ok(materiales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> getMaterialById(@PathVariable Long id) {
        return materialService.getMaterialById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaterialDTO> createMaterial(@Valid @RequestBody MaterialDTO materialDTO, BindingResult result) {
        handleValidationErrors(result);
        MaterialDTO createdMaterial = materialService.createMaterial(materialDTO);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialDTO> updateMaterial(@PathVariable Long id, @Valid @RequestBody MaterialDTO materialDTO, BindingResult result) {
        handleValidationErrors(result);
        MaterialDTO updatedMaterial = materialService.updateMaterial(id, materialDTO);
        return ResponseEntity.ok(updatedMaterial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/asignatura/{asignaturaId}")
    public ResponseEntity<List<MaterialDTO>> getMaterialesByAsignatura(@PathVariable Long asignaturaId) {
        List<MaterialDTO> materiales = materialService.getMaterialesByAsignaturaId(asignaturaId);
        // Opcional: Si la lista está vacía, verificar si la asignatura existe para dar un 404 más claro
        if (materiales.isEmpty() && !asignaturaService.getAsignaturaById(asignaturaId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + asignaturaId);
        }
        return ResponseEntity.ok(materiales);
    }
}