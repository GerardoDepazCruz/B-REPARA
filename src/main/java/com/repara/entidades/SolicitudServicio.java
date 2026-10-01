package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "solicitudes_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion_cliente")
    private DireccionCliente direccionCliente;

    @Column(name = "descripcion_problema", columnDefinition = "TEXT", nullable = false)
    private String descripcionProblema;

    @Column(name = "direccion_servicio", nullable = false)
    private String direccionServicio;

    private BigDecimal latitud;

    private BigDecimal longitud;

    @Column(name = "resumen_ia", columnDefinition = "TEXT")
    private String resumenIa;

    @Column(name = "costo_estimado")
    private BigDecimal costoEstimado;

    @Column(name = "monto_inicial")
    private BigDecimal montoInicial;

    @Column(name = "costo_final")
    private BigDecimal costoFinal;

    @Column(name = "monto_final")
    private BigDecimal montoFinal;

    @Builder.Default
@Column(name = "requiere_repuesto", nullable = false)
private Boolean requiereRepuesto = false;

    @Column(name = "descripcion_repuesto")
    private String descripcionRepuesto;

    @Column(name = "costo_repuesto")
    private BigDecimal costoRepuesto;

    @Builder.Default
@Column(name = "pin_verificado", nullable = false)
private Boolean pinVerificado = false;



    @Column(name = "fecha_pin_verificado")
    private LocalDateTime fechaPinVerificado;

    @Builder.Default
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Estado estado = Estado.EN_CREACION;

    @Column(name = "motivo_cancelacion")
    private String motivoCancelacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cancelador")
    private Usuario cancelador;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @Column(name = "fecha_solicitud", nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @PrePersist
    protected void alCrear() {
        fechaSolicitud = LocalDateTime.now();
    }

    public enum Estado {
        EN_CREACION,
        PENDIENTE_ASIGNACION,
        ACEPTADA,
        PAGO_INICIAL_PENDIENTE,
        EN_CAMINO,
        EN_DIAGNOSTICO,
        DIAGNOSTICO_NO_REPARABLE,
        REPARACION_EN_PROCESO,
        PAGO_FINAL_PENDIENTE,
        COMPLETADO,
        CANCELADO
    }
}