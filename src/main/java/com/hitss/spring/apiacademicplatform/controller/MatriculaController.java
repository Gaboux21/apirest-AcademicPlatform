package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.MatriculaDTO;
import com.hitss.spring.apiacademicplatform.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> getAllMatriculas() {
        List<MatriculaDTO> matriculas = matriculaService.getAllMatriculas();
        return ResponseEntity.ok(matriculas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> getMatriculaById(@PathVariable Long id) {
        return matriculaService.getMatriculaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MatriculaDTO> createMatricula(@Valid @RequestBody MatriculaDTO matriculaDTO, BindingResult result) {
        handleValidationErrors(result);
        MatriculaDTO createdMatricula = matriculaService.createMatricula(matriculaDTO);
        return new ResponseEntity<>(createdMatricula, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> updateMatricula(@PathVariable Long id, @Valid @RequestBody MatriculaDTO matriculaDTO, BindingResult result) {
        handleValidationErrors(result);
        MatriculaDTO updatedMatricula = matriculaService.updateMatricula(id, matriculaDTO);
        return ResponseEntity.ok(updatedMatricula);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable Long id) {
        matriculaService.deleteMatricula(id);
        return ResponseEntity.noContent().build();
    }
}
