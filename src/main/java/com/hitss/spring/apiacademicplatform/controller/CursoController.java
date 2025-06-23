package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.CursoDTO;
import com.hitss.spring.apiacademicplatform.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> getAllCursos() {
        List<CursoDTO> cursos = cursoService.getAllCursos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> getCursoById(@PathVariable Long id) {
        return cursoService.getCursoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursoDTO> createCurso(@Valid @RequestBody CursoDTO cursoDTO, BindingResult result) {
        handleValidationErrors(result);
        CursoDTO createdCurso = cursoService.createCurso(cursoDTO);
        return new ResponseEntity<>(createdCurso, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> updateCurso(@PathVariable Long id, @Valid @RequestBody CursoDTO cursoDTO, BindingResult result) {
        handleValidationErrors(result);
        CursoDTO updatedCurso = cursoService.updateCurso(id, cursoDTO);
        return ResponseEntity.ok(updatedCurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        cursoService.deleteCurso(id);
        return ResponseEntity.noContent().build();
    }
}
