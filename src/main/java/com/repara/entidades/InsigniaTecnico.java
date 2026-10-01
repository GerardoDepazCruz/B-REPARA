package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "insignias_tecnico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsigniaTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insignia")
    private Long idInsignia;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String descripcion;

    @Column(name = "icono_url")
    private String iconoUrl;

    @Column(name = "fecha_asignacion", nullable = false, updatable = false)
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void alCrear() {
        fechaAsignacion = LocalDateTime.now();
    }
}