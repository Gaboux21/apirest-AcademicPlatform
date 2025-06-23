package com.hitss.spring.apiacademicplatform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asignaturas")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Nota> notas;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Material> materiales;
}
