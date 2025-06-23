package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.NotaDTO;
import com.hitss.spring.apiacademicplatform.model.*;
import com.hitss.spring.apiacademicplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private PeriodoLectivoRepository periodoLectivoRepository;

    private NotaDTO convertToDto(Nota nota) {
        return new NotaDTO(
                nota.getId(),
                nota.getEstudiante().getId(),
                nota.getCurso().getId(),
                nota.getAsignatura().getId(),
                nota.getPeriodo().getId(),
                nota.getValor(),
                nota.getFechaEvaluacion(),
                nota.getObservaciones()
        );
    }

    private Nota convertToEntity(NotaDTO notaDTO) {
        Nota nota = new Nota();
        nota.setId(notaDTO.getId());
        nota.setValor(notaDTO.getValor());
        nota.setObservaciones(notaDTO.getObservaciones());

        Estudiante estudiante = estudianteRepository.findById(notaDTO.getEstudianteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + notaDTO.getEstudianteId()));
        nota.setEstudiante(estudiante);

        Asignatura asignatura = asignaturaRepository.findById(notaDTO.getAsignaturaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + notaDTO.getAsignaturaId()));
        nota.setAsignatura(asignatura);

        PeriodoLectivo periodo = periodoLectivoRepository.findById(notaDTO.getPeriodoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo lectivo no encontrado con ID: " + notaDTO.getPeriodoId()));
        nota.setPeriodo(periodo);

        return nota;
    }

    public List<NotaDTO> getAllNotas() {
        return notaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<NotaDTO> getNotaById(Long id) {
        return notaRepository.findById(id)
                .map(this::convertToDto);
    }


    public Nota createNota(NotaDTO notaDTO) {
        Estudiante estudiante = estudianteRepository.findById(notaDTO.getEstudianteId())
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado con ID: " + notaDTO.getEstudianteId()));

        Curso curso = cursoRepository.findById(notaDTO.getCursoId()) // <-- ESTO ES NUEVO
                .orElseThrow(() -> new NoSuchElementException("Curso no encontrado con ID: " + notaDTO.getCursoId()));

        Asignatura asignatura = asignaturaRepository.findById(notaDTO.getAsignaturaId())
                .orElseThrow(() -> new NoSuchElementException("Asignatura no encontrada con ID: " + notaDTO.getAsignaturaId()));

        PeriodoLectivo periodo = periodoLectivoRepository.findById(notaDTO.getPeriodoId())
                .orElseThrow(() -> new NoSuchElementException("Periodo Lectivo no encontrado con ID: " + notaDTO.getPeriodoId()));

        Nota nota = new Nota();
        nota.setEstudiante(estudiante);
        nota.setCurso(curso);
        nota.setAsignatura(asignatura);
        nota.setPeriodo(periodo);
        nota.setValor(notaDTO.getValor());
        nota.setFechaEvaluacion(notaDTO.getFechaEvaluacion());
        nota.setObservaciones(notaDTO.getObservaciones());

        return notaRepository.save(nota);
    }

    public NotaDTO updateNota(Long id, NotaDTO notaDTO) {
        return notaRepository.findById(id)
                .map(existingNota -> {
                    existingNota.setValor(notaDTO.getValor());
                    existingNota.setObservaciones(notaDTO.getObservaciones());

                    if (!existingNota.getEstudiante().getId().equals(notaDTO.getEstudianteId())) {
                        Estudiante nuevoEstudiante = estudianteRepository.findById(notaDTO.getEstudianteId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + notaDTO.getEstudianteId()));
                        existingNota.setEstudiante(nuevoEstudiante);
                    }
                    if (!existingNota.getAsignatura().getId().equals(notaDTO.getAsignaturaId())) {
                        Asignatura nuevaAsignatura = asignaturaRepository.findById(notaDTO.getAsignaturaId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + notaDTO.getAsignaturaId()));
                        existingNota.setAsignatura(nuevaAsignatura);
                    }
                    if (!existingNota.getPeriodo().getId().equals(notaDTO.getPeriodoId())) {
                        PeriodoLectivo nuevoPeriodo = periodoLectivoRepository.findById(notaDTO.getPeriodoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo lectivo no encontrado con ID: " + notaDTO.getPeriodoId()));
                        existingNota.setPeriodo(nuevoPeriodo);
                    }
                    return convertToDto(notaRepository.save(existingNota));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada con ID: " + id));
    }

    public void deleteNota(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada con ID: " + id);
        }
        notaRepository.deleteById(id);
    }

    public List<NotaDTO> getNotasByEstudianteId(Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NotaDTO> getNotasByAsignaturaId(Long asignaturaId) {
        return notaRepository.findByAsignaturaId(asignaturaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
