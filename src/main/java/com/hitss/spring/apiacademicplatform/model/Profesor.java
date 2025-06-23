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
@Table(name = "profesores")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String especialidad;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asignatura> asignaturasImpartidas;
}
