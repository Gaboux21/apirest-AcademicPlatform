package com.hitss.spring.apiacademicplatform.dto.auth;

import com.hitss.spring.apiacademicplatform.model.Rol;
import com.hitss.spring.apiacademicplatform.model.Usuario;
import com.hitss.spring.apiacademicplatform.repository.UsuarioRepository;
import com.hitss.spring.apiacademicplatform.security.jwt.JwtUtils;
import com.hitss.spring.apiacademicplatform.security.services.UserDetailsImpl;
import com.hitss.spring.apiacademicplatform.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                Rol.valueOf(userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")) // Obtener el rol
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (usuarioRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El Email ya está en uso!");
        }

        Usuario usuario = new Usuario(
                signUpRequest.getNombre(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getRol()
        );

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}
