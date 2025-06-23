package com.hitss.spring.apiacademicplatform.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialNotaEstudianteDTO {
    private Long cursoId;
    private String nombreCurso;
    private Long asignaturaId;
    private String nombreAsignatura;
    private Double nota;
    private String fechaEvaluacion;
}
