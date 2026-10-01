package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "postulaciones_tecnico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulacionTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "especialidad_declarada", nullable = false)
    private String especialidadDeclarada;

    @Column(name = "anios_experiencia")
    private Integer aniosExperiencia;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "certificados_url")
    private String certificadosUrl;

    @Column(name = "dni_documento_url")
    private String dniDocumentoUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @Column(name = "motivo_rechazo")
    private String motivoRechazo;

    @Column(name = "fecha_postulacion", nullable = false, updatable = false)
    private LocalDateTime fechaPostulacion;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_revisor")
    private Usuario adminRevisor;

    @PrePersist
    protected void alCrear() {
        fechaPostulacion = LocalDateTime.now();
    }

    public enum Estado {
        PENDIENTE, ACEPTADO, RECHAZADO
    }
}