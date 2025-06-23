package com.hitss.spring.apiacademicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorDTO {
    private Long id;

    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Long usuarioId;

    @NotBlank(message = "La especialidad no puede estar vacía")
    private String especialidad;
}
