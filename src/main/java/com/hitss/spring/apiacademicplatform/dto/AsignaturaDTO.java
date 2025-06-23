package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {
    private Long id;

    @NotBlank(message = "El nombre de la asignatura no puede estar vacío")
    private String nombre;

    @NotNull(message = "El ID del profesor no puede ser nulo")
    private Long profesorId;

    @NotNull(message = "El ID del curso no puede ser nulo")
    private Long cursoId;
}
