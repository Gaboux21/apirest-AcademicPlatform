package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.MatriculaDTO;
import com.hitss.spring.apiacademicplatform.model.Curso;
import com.hitss.spring.apiacademicplatform.model.Estudiante;
import com.hitss.spring.apiacademicplatform.model.Matricula;
import com.hitss.spring.apiacademicplatform.model.PeriodoLectivo;
import com.hitss.spring.apiacademicplatform.repository.CursoRepository;
import com.hitss.spring.apiacademicplatform.repository.EstudianteRepository;
import com.hitss.spring.apiacademicplatform.repository.MatriculaRepository;
import com.hitss.spring.apiacademicplatform.repository.PeriodoLectivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private PeriodoLectivoRepository periodoLectivoRepository;

    private MatriculaDTO convertToDto(Matricula matricula) {
        return new MatriculaDTO(
                matricula.getId(),
                matricula.getEstudiante().getId(),
                matricula.getCurso().getId(),
                matricula.getPeriodo().getId()
        );
    }

    private Matricula convertToEntity(MatriculaDTO matriculaDTO) {
        Matricula matricula = new Matricula();
        matricula.setId(matriculaDTO.getId());

        Estudiante estudiante = estudianteRepository.findById(matriculaDTO.getEstudianteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + matriculaDTO.getEstudianteId()));
        matricula.setEstudiante(estudiante);

        Curso curso = cursoRepository.findById(matriculaDTO.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado con ID: " + matriculaDTO.getCursoId()));
        matricula.setCurso(curso);

        PeriodoLectivo periodo = periodoLectivoRepository.findById(matriculaDTO.getPeriodoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo lectivo no encontrado con ID: " + matriculaDTO.getPeriodoId()));
        matricula.setPeriodo(periodo);

        return matricula;
    }

    public List<MatriculaDTO> getAllMatriculas() {
        return matriculaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<MatriculaDTO> getMatriculaById(Long id) {
        return matriculaRepository.findById(id)
                .map(this::convertToDto);
    }

    public MatriculaDTO createMatricula(MatriculaDTO matriculaDTO) {
        Matricula matricula = convertToEntity(matriculaDTO);
        return convertToDto(matriculaRepository.save(matricula));
    }

    public MatriculaDTO updateMatricula(Long id, MatriculaDTO matriculaDTO) {
        return matriculaRepository.findById(id)
                .map(existingMatricula -> {
                    if (!existingMatricula.getEstudiante().getId().equals(matriculaDTO.getEstudianteId())) {
                        Estudiante nuevoEstudiante = estudianteRepository.findById(matriculaDTO.getEstudianteId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + matriculaDTO.getEstudianteId()));
                        existingMatricula.setEstudiante(nuevoEstudiante);
                    }
                    if (!existingMatricula.getCurso().getId().equals(matriculaDTO.getCursoId())) {
                        Curso nuevoCurso = cursoRepository.findById(matriculaDTO.getCursoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado con ID: " + matriculaDTO.getCursoId()));
                        existingMatricula.setCurso(nuevoCurso);
                    }
                    if (!existingMatricula.getPeriodo().getId().equals(matriculaDTO.getPeriodoId())) {
                        PeriodoLectivo nuevoPeriodo = periodoLectivoRepository.findById(matriculaDTO.getPeriodoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo lectivo no encontrado con ID: " + matriculaDTO.getPeriodoId()));
                        existingMatricula.setPeriodo(nuevoPeriodo);
                    }
                    return convertToDto(matriculaRepository.save(existingMatricula));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula no encontrada con ID: " + id));
    }

    public void deleteMatricula(Long id) {
        if (!matriculaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula no encontrada con ID: " + id);
        }
        matriculaRepository.deleteById(id);
    }

    public List<MatriculaDTO> getMatriculasByEstudianteId(Long estudianteId) {
        return matriculaRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
