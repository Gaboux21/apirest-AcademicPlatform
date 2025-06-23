package com.hitss.spring.apiacademicplatform.controller;

import com.hitss.spring.apiacademicplatform.dto.reportes.HistorialEstudianteResponse;
import com.hitss.spring.apiacademicplatform.dto.reportes.NotaPromedioCursoAsignaturaDTO;
import com.hitss.spring.apiacademicplatform.dto.reportes.ReporteFinalCursoDTO;
import com.hitss.spring.apiacademicplatform.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;


    @GetMapping("/notas-promedio")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<List<NotaPromedioCursoAsignaturaDTO>> getNotasPromedio() {
        List<NotaPromedioCursoAsignaturaDTO> report = reporteService.getNotasPromedioPorCursoYAsignatura();
        return ResponseEntity.ok(report);
    }


    @GetMapping("/historial-estudiante/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or (hasRole('ESTUDIANTE') and @userSecurity.hasStudentId(authentication, #id))")
    public ResponseEntity<?> getHistorialEstudiante(@PathVariable Long id) {
        try {
            HistorialEstudianteResponse historial = reporteService.getHistorialEstudiante(id);
            return ResponseEntity.ok(historial);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/reporte-final/{cursoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<List<ReporteFinalCursoDTO>> getReporteFinalCurso(@PathVariable Long cursoId) {
        try {
            List<ReporteFinalCursoDTO> report = reporteService.getReporteFinalCurso(cursoId);
            return ResponseEntity.ok(report);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
