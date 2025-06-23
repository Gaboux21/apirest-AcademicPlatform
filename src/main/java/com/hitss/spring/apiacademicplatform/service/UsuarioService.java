package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.UsuarioDTO;
import com.hitss.spring.apiacademicplatform.model.Usuario;
import com.hitss.spring.apiacademicplatform.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UsuarioDTO convertToDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                null,
                usuario.getRol()
        );
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setRol(usuarioDTO.getRol());
        return usuario;
    }

    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDto);
    }

    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        Usuario usuario = convertToEntity(usuarioDTO);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return convertToDto(usuarioRepository.save(usuario));
    }

    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNombre(usuarioDTO.getNombre());
                    existingUser.setEmail(usuarioDTO.getEmail());
                    existingUser.setRol(usuarioDTO.getRol());
                    if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
                    }
                    return convertToDto(usuarioRepository.save(existingUser));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
