package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaDTO {
    private Long id;

    @NotNull(message = "El ID del estudiante no puede ser nulo")
    private Long estudianteId;

    @NotNull(message = "El ID del curso no puede ser nulo")
    private Long cursoId;

    @NotNull(message = "El ID del periodo lectivo no puede ser nulo")
    private Long periodoId;
}
