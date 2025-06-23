package com.hitss.spring.apiacademicplatform.repository;

import com.hitss.spring.apiacademicplatform.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByProfesorId(Long profesorId);
    List<Asignatura> findByCursoId(Long cursoId);
}
