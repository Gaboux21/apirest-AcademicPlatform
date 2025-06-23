package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Long id;

    @NotBlank(message = "El nombre del curso no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El año académico no puede estar vacío")
    private String anoAcademico;
}
