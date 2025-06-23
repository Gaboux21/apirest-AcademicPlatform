package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.AsignaturaDTO;
import com.hitss.spring.apiacademicplatform.model.Asignatura;
import com.hitss.spring.apiacademicplatform.model.Curso;
import com.hitss.spring.apiacademicplatform.model.Profesor;
import com.hitss.spring.apiacademicplatform.repository.AsignaturaRepository;
import com.hitss.spring.apiacademicplatform.repository.CursoRepository;
import com.hitss.spring.apiacademicplatform.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private CursoRepository cursoRepository;

    private AsignaturaDTO convertToDto(Asignatura asignatura) {
        return new AsignaturaDTO(
                asignatura.getId(),
                asignatura.getNombre(),
                asignatura.getProfesor().getId(),
                asignatura.getCurso().getId()
        );
    }

    private Asignatura convertToEntity(AsignaturaDTO asignaturaDTO) {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(asignaturaDTO.getId());
        asignatura.setNombre(asignaturaDTO.getNombre());

        Profesor profesor = profesorRepository.findById(asignaturaDTO.getProfesorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + asignaturaDTO.getProfesorId()));
        asignatura.setProfesor(profesor);

        Curso curso = cursoRepository.findById(asignaturaDTO.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado con ID: " + asignaturaDTO.getCursoId()));
        asignatura.setCurso(curso);

        return asignatura;
    }

    public List<AsignaturaDTO> getAllAsignaturas() {
        return asignaturaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<AsignaturaDTO> getAsignaturaById(Long id) {
        return asignaturaRepository.findById(id)
                .map(this::convertToDto);
    }

    public AsignaturaDTO createAsignatura(AsignaturaDTO asignaturaDTO) {
        Asignatura asignatura = convertToEntity(asignaturaDTO);
        return convertToDto(asignaturaRepository.save(asignatura));
    }

    public AsignaturaDTO updateAsignatura(Long id, AsignaturaDTO asignaturaDTO) {
        return asignaturaRepository.findById(id)
                .map(existingAsignatura -> {
                    existingAsignatura.setNombre(asignaturaDTO.getNombre());

                    if (!existingAsignatura.getProfesor().getId().equals(asignaturaDTO.getProfesorId())) {
                        Profesor nuevoProfesor = profesorRepository.findById(asignaturaDTO.getProfesorId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + asignaturaDTO.getProfesorId()));
                        existingAsignatura.setProfesor(nuevoProfesor);
                    }

                    if (!existingAsignatura.getCurso().getId().equals(asignaturaDTO.getCursoId())) {
                        Curso nuevoCurso = cursoRepository.findById(asignaturaDTO.getCursoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado con ID: " + asignaturaDTO.getCursoId()));
                        existingAsignatura.setCurso(nuevoCurso);
                    }
                    return convertToDto(asignaturaRepository.save(existingAsignatura));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + id));
    }

    public void deleteAsignatura(Long id) {
        if (!asignaturaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + id);
        }
        asignaturaRepository.deleteById(id);
    }

    public List<AsignaturaDTO> getAsignaturasByProfesorId(Long profesorId) {
        return asignaturaRepository.findByProfesorId(profesorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
