package com.hitss.spring.apiacademicplatform.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteFinalCursoDTO {
    private Long estudianteId;
    private String nombreEstudiante;
    private String emailEstudiante;
    private Double promedioGeneralCurso;

    public ReporteFinalCursoDTO(Long id, Double promedio) {
    }
}
