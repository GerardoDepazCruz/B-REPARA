package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "evidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evidencia")
    private Long idEvidencia;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_archivo", nullable = false)
    private TipoArchivo tipoArchivo;

    @Column(name = "url_archivo", nullable = false)
    private String urlArchivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Etapa etapa;

    private String descripcion;

    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void alCrear() {
        fechaSubida = LocalDateTime.now();
    }

    public enum TipoArchivo {
        FOTO, VIDEO
    }

    public enum Etapa {
        DESCRIPCION_PROBLEMA,
        DIAGNOSTICO,
        REPARACION_TERMINADA,
        FALLA_NO_REPARABLE
    }
}