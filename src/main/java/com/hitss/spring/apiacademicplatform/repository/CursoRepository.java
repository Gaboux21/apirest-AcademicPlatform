package com.hitss.spring.apiacademicplatform.repository;

import com.hitss.spring.apiacademicplatform.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
