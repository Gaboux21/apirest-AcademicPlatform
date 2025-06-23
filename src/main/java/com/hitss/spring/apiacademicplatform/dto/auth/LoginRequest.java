package com.hitss.spring.apiacademicplatform.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}