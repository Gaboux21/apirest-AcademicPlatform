package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoLectivoDTO {
    private Long id;

    @NotBlank(message = "El nombre del periodo lectivo no puede estar vacío")
    private String nombre;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula")
    private LocalDate fechaFin;
}
