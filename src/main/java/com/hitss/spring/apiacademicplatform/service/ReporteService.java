package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.reportes.HistorialEstudianteResponse;
import com.hitss.spring.apiacademicplatform.dto.reportes.HistorialNotaEstudianteDTO;
import com.hitss.spring.apiacademicplatform.dto.reportes.NotaPromedioCursoAsignaturaDTO;
import com.hitss.spring.apiacademicplatform.dto.reportes.ReporteFinalCursoDTO;
import com.hitss.spring.apiacademicplatform.model.*;
import com.hitss.spring.apiacademicplatform.repository.AsignaturaRepository;
import com.hitss.spring.apiacademicplatform.repository.CursoRepository;
import com.hitss.spring.apiacademicplatform.repository.NotaRepository;
import com.hitss.spring.apiacademicplatform.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private NotaRepository notaRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<NotaPromedioCursoAsignaturaDTO> getNotasPromedioPorCursoYAsignatura() {
        List<Nota> todasLasNotas = notaRepository.findAll();

        return todasLasNotas.stream()
                .collect(Collectors.groupingBy(
                        nota -> new TupleCursoAsignatura(nota.getCurso().getId(), nota.getAsignatura().getId()),
                        Collectors.averagingDouble(Nota::getValor)
                ))
                .entrySet().stream()
                .map(entry -> {
                    Long cursoId = entry.getKey().getCursoId();
                    Long asignaturaId = entry.getKey().getAsignaturaId();
                    Double promedio = entry.getValue();

                    Optional<Curso> curso = cursoRepository.findById(cursoId);
                    Optional<Asignatura> asignatura = asignaturaRepository.findById(asignaturaId);

                    return new NotaPromedioCursoAsignaturaDTO(
                            cursoId,
                            curso.map(Curso::getNombre).orElse("Desconocido"),
                            asignaturaId,
                            asignatura.map(Asignatura::getNombre).orElse("Desconocido"),
                            promedio
                    );
                })
                .collect(Collectors.toList());
    }


    private static class TupleCursoAsignatura {
        private Long cursoId;
        private Long asignaturaId;

        public TupleCursoAsignatura(Long cursoId, Long asignaturaId) {
            this.cursoId = cursoId;
            this.asignaturaId = asignaturaId;
        }

        public Long getCursoId() { return cursoId; }
        public Long getAsignaturaId() { return asignaturaId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TupleCursoAsignatura that = (TupleCursoAsignatura) o;
            return cursoId.equals(that.cursoId) && asignaturaId.equals(that.asignaturaId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cursoId, asignaturaId);
        }
    }


    public HistorialEstudianteResponse getHistorialEstudiante(Long estudianteId) {
        Usuario estudiante = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado con ID: " + estudianteId));

        List<Nota> notasDelEstudiante = notaRepository.findByEstudianteId(estudianteId);

        List<HistorialNotaEstudianteDTO> notasDTO = notasDelEstudiante.stream()
                .map(nota -> new HistorialNotaEstudianteDTO(
                        nota.getCurso().getId(),
                        nota.getCurso().getNombre(),
                        nota.getAsignatura().getId(),
                        nota.getAsignatura().getNombre(),
                        nota.getValor(),
                        nota.getFechaEvaluacion().toString()
                ))
                .collect(Collectors.toList());

        return new HistorialEstudianteResponse(estudiante.getId(), estudiante.getNombre(), estudiante.getEmail(), notasDTO);
    }

    public List<ReporteFinalCursoDTO> getReporteFinalCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new NoSuchElementException("Curso no encontrado con ID: " + cursoId));

        List<Nota> notasDelCurso = notaRepository.findByCursoId(cursoId);

        return notasDelCurso.stream()
                .collect(Collectors.groupingBy(
                        Nota::getEstudiante,
                        Collectors.averagingDouble(Nota::getValor)
                ))
                .entrySet().stream()
                .map(entry -> {
                    Usuario estudiante = entry.getKey().getUsuario();
                    Double promedio = entry.getValue();
                    return new ReporteFinalCursoDTO(
                            estudiante.getId(),
                            estudiante.getNombre(),
                            estudiante.getEmail(),
                            promedio
                    );
                })
                .collect(Collectors.toList());
    }
}
