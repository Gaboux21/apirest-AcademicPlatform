package com.hitss.spring.apiacademicplatform.repository;

import com.hitss.spring.apiacademicplatform.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByAsignaturaId(Long asignaturaId);
    List<Material> findByProfesorId(Long profesorId);
}
