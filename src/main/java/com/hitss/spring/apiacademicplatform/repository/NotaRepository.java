package com.hitss.spring.apiacademicplatform.repository;

import com.hitss.spring.apiacademicplatform.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByEstudianteId(Long estudianteId);
    List<Nota> findByAsignaturaId(Long asignaturaId);
    List<Nota> findByEstudianteIdAndAsignaturaId(Long estudianteId, Long asignaturaId);
    List<Nota> findByCursoId(Long cursoId);
}
