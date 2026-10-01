package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tecnicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Long idTecnico;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_postulacion_origen", nullable = false)
    private PostulacionTecnico postulacionOrigen;

    @Builder.Default
@Column(name = "calificacion_promedio", nullable = false)
private BigDecimal calificacionPromedio = BigDecimal.ZERO;

    @Builder.Default
@Column(name = "total_servicios", nullable = false)
private Integer totalServicios = 0;

    @Builder.Default
@Column(name = "total_ganancias", nullable = false)
private BigDecimal totalGanancias = BigDecimal.ZERO;

    @Builder.Default
@Column(nullable = false)
private Boolean disponible = true;

    @Builder.Default
@Column(name = "radio_cobertura_km")
private BigDecimal radioCoberturaKm = new BigDecimal("10.00");

    @Column(name = "latitud_actual")
    private BigDecimal latitudActual;

    @Column(name = "longitud_actual")
    private BigDecimal longitudActual;

    @Column(name = "ultima_actualizacion_ubicacion")
    private LocalDateTime ultimaActualizacionUbicacion;

    @Builder.Default
@Enumerated(EnumType.STRING)
@Column(name = "estado_verificacion")
private EstadoVerificacion estadoVerificacion = EstadoVerificacion.VERIFICADO;



    @Builder.Default
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Estado estado = Estado.ACTIVO;

    @Column(name = "fecha_aprobacion", nullable = false, updatable = false)
    private LocalDateTime fechaAprobacion;

    @PrePersist
    protected void alCrear() {
        fechaAprobacion = LocalDateTime.now();
    }

    public enum Estado {
        ACTIVO, SUSPENDIDO, INACTIVO
    }

    public enum EstadoVerificacion {
        PENDIENTE, VERIFICADO, RECHAZADO
    }
}