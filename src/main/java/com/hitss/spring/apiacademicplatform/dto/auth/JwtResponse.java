package com.hitss.spring.apiacademicplatform.dto.auth;

import com.hitss.spring.apiacademicplatform.model.Rol;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Rol rol;

    public JwtResponse(String accessToken, Long id, String username, String email, Rol rol) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
    }
}
