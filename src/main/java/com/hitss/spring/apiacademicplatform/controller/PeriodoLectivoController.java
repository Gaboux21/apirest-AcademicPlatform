package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.PeriodoLectivoDTO;
import com.hitss.spring.apiacademicplatform.service.PeriodoLectivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoLectivoController {

    @Autowired
    private PeriodoLectivoService periodoLectivoService;

    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<PeriodoLectivoDTO>> getAllPeriodos() {
        List<PeriodoLectivoDTO> periodos = periodoLectivoService.getAllPeriodos();
        return ResponseEntity.ok(periodos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoLectivoDTO> getPeriodoById(@PathVariable Long id) {
        return periodoLectivoService.getPeriodoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PeriodoLectivoDTO> createPeriodo(@Valid @RequestBody PeriodoLectivoDTO periodoDTO, BindingResult result) {
        handleValidationErrors(result);
        PeriodoLectivoDTO createdPeriodo = periodoLectivoService.createPeriodo(periodoDTO);
        return new ResponseEntity<>(createdPeriodo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodoLectivoDTO> updatePeriodo(@PathVariable Long id, @Valid @RequestBody PeriodoLectivoDTO periodoDTO, BindingResult result) {
        handleValidationErrors(result);
        PeriodoLectivoDTO updatedPeriodo = periodoLectivoService.updatePeriodo(id, periodoDTO);
        return ResponseEntity.ok(updatedPeriodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriodo(@PathVariable Long id) {
        periodoLectivoService.deletePeriodo(id);
        return ResponseEntity.noContent().build();
    }
}
