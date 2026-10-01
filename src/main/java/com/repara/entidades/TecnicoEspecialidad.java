package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tecnico_especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(TecnicoEspecialidadId.class)
public class TecnicoEspecialidad {

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Builder.Default
    @Column(name = "anios_experiencia")
    private Integer aniosExperiencia = 0;

    @Column(name = "certificado_url")
    private String certificadoUrl;

    @Column(name = "fecha_asignacion", nullable = false, updatable = false)
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void alCrear() {
        fechaAsignacion = LocalDateTime.now();
    }
}