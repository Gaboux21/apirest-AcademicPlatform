package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.EstudianteDTO;
import com.hitss.spring.apiacademicplatform.model.Estudiante;
import com.hitss.spring.apiacademicplatform.model.Usuario;
import com.hitss.spring.apiacademicplatform.repository.EstudianteRepository;
import com.hitss.spring.apiacademicplatform.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private EstudianteDTO convertToDto(Estudiante estudiante) {
        return new EstudianteDTO(
                estudiante.getId(),
                estudiante.getUsuario().getId(),
                estudiante.getCodigoMatricula()
        );
    }

    private Estudiante convertToEntity(EstudianteDTO estudianteDTO) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteDTO.getId());
        estudiante.setCodigoMatricula(estudianteDTO.getCodigoMatricula());
        Usuario usuario = usuarioRepository.findById(estudianteDTO.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + estudianteDTO.getUsuarioId()));
        estudiante.setUsuario(usuario);
        return estudiante;
    }

    public List<EstudianteDTO> getAllEstudiantes() {
        return estudianteRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<EstudianteDTO> getEstudianteById(Long id) {
        return estudianteRepository.findById(id)
                .map(this::convertToDto);
    }

    public EstudianteDTO createEstudiante(EstudianteDTO estudianteDTO) {
        // Asegúrate de que el usuario exista y que no esté ya asociado a un estudiante
        if (estudianteRepository.findByUsuarioId(estudianteDTO.getUsuarioId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este usuario ya está registrado como estudiante.");
        }
        if (estudianteRepository.findByCodigoMatricula(estudianteDTO.getCodigoMatricula()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El código de matrícula ya existe.");
        }
        Estudiante estudiante = convertToEntity(estudianteDTO);
        return convertToDto(estudianteRepository.save(estudiante));
    }

    public EstudianteDTO updateEstudiante(Long id, EstudianteDTO estudianteDTO) {
        return estudianteRepository.findById(id)
                .map(existingEstudiante -> {
                    if (!existingEstudiante.getUsuario().getId().equals(estudianteDTO.getUsuarioId())) {
                        if (estudianteRepository.findByUsuarioId(estudianteDTO.getUsuarioId()).isPresent()) {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo usuario ya está registrado como estudiante.");
                        }
                        Usuario nuevoUsuario = usuarioRepository.findById(estudianteDTO.getUsuarioId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + estudianteDTO.getUsuarioId()));
                        existingEstudiante.setUsuario(nuevoUsuario);
                    }
                    if (!existingEstudiante.getCodigoMatricula().equals(estudianteDTO.getCodigoMatricula()) &&
                            estudianteRepository.findByCodigoMatricula(estudianteDTO.getCodigoMatricula()).isPresent()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo código de matrícula ya existe.");
                    }
                    existingEstudiante.setCodigoMatricula(estudianteDTO.getCodigoMatricula());
                    return convertToDto(estudianteRepository.save(existingEstudiante));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + id));
    }

    public void deleteEstudiante(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}
