package com.hitss.spring.apiacademicplatform.dto.auth;

import com.hitss.spring.apiacademicplatform.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    private Rol rol;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
