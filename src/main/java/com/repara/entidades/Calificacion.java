package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "calificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private Long idCalificacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_calificador", nullable = false)
    private Usuario calificador;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_calificado", nullable = false)
    private Usuario calificado;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_calificador", nullable = false)
    private TipoCalificador tipoCalificador = TipoCalificador.CLIENTE;

    @Column(nullable = false)
private Byte puntuacion;

    @Column(length = 500)
    private String comentario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    protected void alCrear() {
        fecha = LocalDateTime.now();
    }

    public enum TipoCalificador {
        CLIENTE, TECNICO
    }
}