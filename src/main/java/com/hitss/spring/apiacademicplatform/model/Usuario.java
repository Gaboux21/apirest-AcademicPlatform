package com.hitss.spring.apiacademicplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "usuarios") // Nombre de la tabla en la base de datos
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank // Aunque no se guarde en DB, es buena práctica para validación a nivel de la entidad
    @Size(max = 50)
    @Column(nullable = false) // <--- ¡ASEGÚRATE DE ESTA LÍNEA!
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(nullable = false, unique = true) // <--- ¡Y ESTA OTRA LÍNEA! Es crucial para el email.
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false) // <--- ¡Y ESTA OTRA PARA LA CONTRASEÑA!
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // <--- ¡Y ESTA PARA EL ROL!
    private Rol rol;

    public Usuario(String nombre, String email, String password, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
