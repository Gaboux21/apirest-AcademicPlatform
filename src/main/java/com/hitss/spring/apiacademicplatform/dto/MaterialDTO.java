package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDTO {
    private Long id;

    @NotBlank(message = "El título del material no puede estar vacío")
    private String titulo;

    private String descripcion;

    @NotBlank(message = "La URL del archivo no puede estar vacía")
    private String archivoUrl;

    @NotNull(message = "El ID de la asignatura no puede ser nulo")
    private Long asignaturaId;

    @NotNull(message = "El ID del profesor no puede ser nulo")
    private Long profesorId;
}