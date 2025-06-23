package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {
    private Long id;

    @NotNull(message = "El ID del estudiante no puede ser nulo")
    private Long estudianteId;

    @NotNull(message = "El ID del curso no puede ser nulo")
    private Long cursoId;

    @NotNull(message = "El ID de la asignatura no puede ser nulo")
    private Long asignaturaId;

    @NotNull(message = "El ID del periodo lectivo no puede ser nulo")
    private Long periodoId;

    @NotNull(message = "El valor de la nota no puede ser nulo")
    @DecimalMin(value = "0.0", message = "La nota debe ser mayor o igual a 0.0")
    private Double valor;

    @NotNull(message = "La fecha de evaluación no puede ser nula")
    private LocalDate fechaEvaluacion;

    private String observaciones;
}
