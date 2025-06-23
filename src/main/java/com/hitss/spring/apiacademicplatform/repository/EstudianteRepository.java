package com.hitss.spring.apiacademicplatform.repository;

import com.hitss.spring.apiacademicplatform.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioId(Long usuarioId);
    Optional<Estudiante> findByCodigoMatricula(String codigoMatricula);
}
