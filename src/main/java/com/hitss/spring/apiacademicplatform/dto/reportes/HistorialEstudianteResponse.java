package com.hitss.spring.apiacademicplatform.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstudianteResponse {
    private Long estudianteId;
    private String nombreEstudiante;
    private String emailEstudiante;
    private List<HistorialNotaEstudianteDTO> notas;
}
