package com.hitss.spring.apiacademicplatform.repository;

import com.hitss.spring.apiacademicplatform.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByEstudianteId(Long estudianteId);
    List<Matricula> findByCursoId(Long cursoId);
    List<Matricula> findByPeriodoId(Long periodoId);
}
