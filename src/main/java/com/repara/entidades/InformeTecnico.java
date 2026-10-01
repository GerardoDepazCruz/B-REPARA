package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "informes_tecnico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformeTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informe")
    private Long idInforme;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    @Column(name = "resumen_trabajo", columnDefinition = "TEXT", nullable = false)
    private String resumenTrabajo;

    @Column(name = "diagnostico_detallado", columnDefinition = "TEXT")
    private String diagnosticoDetallado;

    @Column(name = "acciones_realizadas", columnDefinition = "TEXT")
    private String accionesRealizadas;

    @Column(name = "tiempo_estimado_min")
    private Integer tiempoEstimadoMin;

    @Column(name = "tiempo_real_min")
    private Integer tiempoRealMin;

    @Column(name = "mano_obra_costo")
    private BigDecimal manoObraCosto;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "recomendaciones_cliente", columnDefinition = "TEXT")
    private String recomendacionesCliente;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_final", nullable = false)
    private EstadoFinal estadoFinal = EstadoFinal.REPARADO;

    @Builder.Default
    @Column(name = "cliente_conforme", nullable = false)
    private Boolean clienteConforme = false;

    @Column(name = "fecha_conformidad")
    private LocalDateTime fechaConformidad;

    @Column(name = "fecha_informe", nullable = false, updatable = false)
    private LocalDateTime fechaInforme;

    @Builder.Default
    @Column(name = "visible_cliente", nullable = false)
    private Boolean visibleCliente = true;

    @JsonIgnore
    @OneToMany(mappedBy = "informe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepuestoUtilizado> repuestos;

    @PrePersist
    protected void alCrear() {
        fechaInforme = LocalDateTime.now();
    }

    public enum EstadoFinal {
        REPARADO, PARCIALMENTE_REPARADO, NO_REPARABLE, REQUIERE_REPUESTO
    }
}