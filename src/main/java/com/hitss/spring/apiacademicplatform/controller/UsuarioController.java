package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.UsuarioDTO;
import com.hitss.spring.apiacademicplatform.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Helper para manejar errores de validación
    private void handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        handleValidationErrors(result);
        UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDTO);
        return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        handleValidationErrors(result);
        UsuarioDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
