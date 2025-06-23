package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.CursoDTO;
import com.hitss.spring.apiacademicplatform.model.Curso;
import com.hitss.spring.apiacademicplatform.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    private CursoDTO convertToDto(Curso curso) {
        return new CursoDTO(
                curso.getId(),
                curso.getNombre(),
                curso.getAnoAcademico()
        );
    }

    private Curso convertToEntity(CursoDTO cursoDTO) {
        Curso curso = new Curso();
        curso.setId(cursoDTO.getId());
        curso.setNombre(cursoDTO.getNombre());
        curso.setAnoAcademico(cursoDTO.getAnoAcademico());
        return curso;
    }

    public List<CursoDTO> getAllCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CursoDTO> getCursoById(Long id) {
        return cursoRepository.findById(id)
                .map(this::convertToDto);
    }

    public CursoDTO createCurso(CursoDTO cursoDTO) {
        Curso curso = convertToEntity(cursoDTO);
        return convertToDto(cursoRepository.save(curso));
    }

    public CursoDTO updateCurso(Long id, CursoDTO cursoDTO) {
        return cursoRepository.findById(id)
                .map(existingCurso -> {
                    existingCurso.setNombre(cursoDTO.getNombre());
                    existingCurso.setAnoAcademico(cursoDTO.getAnoAcademico());
                    return convertToDto(cursoRepository.save(existingCurso));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado con ID: " + id));
    }

    public void deleteCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}
