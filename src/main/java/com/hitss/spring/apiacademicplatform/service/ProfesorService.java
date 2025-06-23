package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.ProfesorDTO;
import com.hitss.spring.apiacademicplatform.model.Profesor;
import com.hitss.spring.apiacademicplatform.model.Usuario;
import com.hitss.spring.apiacademicplatform.repository.ProfesorRepository;
import com.hitss.spring.apiacademicplatform.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private ProfesorDTO convertToDto(Profesor profesor) {
        return new ProfesorDTO(
                profesor.getId(),
                profesor.getUsuario().getId(),
                profesor.getEspecialidad()
        );
    }

    private Profesor convertToEntity(ProfesorDTO profesorDTO) {
        Profesor profesor = new Profesor();
        profesor.setId(profesorDTO.getId());
        profesor.setEspecialidad(profesorDTO.getEspecialidad());
        Usuario usuario = usuarioRepository.findById(profesorDTO.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + profesorDTO.getUsuarioId()));
        profesor.setUsuario(usuario);
        return profesor;
    }

    public List<ProfesorDTO> getAllProfesores() {
        return profesorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ProfesorDTO> getProfesorById(Long id) {
        return profesorRepository.findById(id)
                .map(this::convertToDto);
    }

    public ProfesorDTO createProfesor(ProfesorDTO profesorDTO) {
        if (profesorRepository.findByUsuarioId(profesorDTO.getUsuarioId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este usuario ya está registrado como profesor.");
        }
        Profesor profesor = convertToEntity(profesorDTO);
        return convertToDto(profesorRepository.save(profesor));
    }

    public ProfesorDTO updateProfesor(Long id, ProfesorDTO profesorDTO) {
        return profesorRepository.findById(id)
                .map(existingProfesor -> {
                    if (!existingProfesor.getUsuario().getId().equals(profesorDTO.getUsuarioId())) {
                        if (profesorRepository.findByUsuarioId(profesorDTO.getUsuarioId()).isPresent()) {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo usuario ya está registrado como profesor.");
                        }
                        Usuario nuevoUsuario = usuarioRepository.findById(profesorDTO.getUsuarioId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + profesorDTO.getUsuarioId()));
                        existingProfesor.setUsuario(nuevoUsuario);
                    }
                    existingProfesor.setEspecialidad(profesorDTO.getEspecialidad());
                    return convertToDto(profesorRepository.save(existingProfesor));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + id));
    }

    public void deleteProfesor(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + id);
        }
        profesorRepository.deleteById(id);
    }
}
