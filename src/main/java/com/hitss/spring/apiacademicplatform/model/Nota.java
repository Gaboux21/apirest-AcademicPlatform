package com.hitss.spring.apiacademicplatform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoLectivo periodo;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}