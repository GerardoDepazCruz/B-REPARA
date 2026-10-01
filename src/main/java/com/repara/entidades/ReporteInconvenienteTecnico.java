package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reportes_inconvenientes_tecnico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteInconvenienteTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte_incon")
    private Long idReporteIncon;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_inconveniente", nullable = false)
    private TipoInconveniente tipoInconveniente;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "evidencia_url")
    private String evidenciaUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ABIERTO;

    @Column(name = "fecha_reporte", nullable = false, updatable = false)
    private LocalDateTime fechaReporte;

    @PrePersist
    protected void alCrear() {
        fechaReporte = LocalDateTime.now();
    }

    public enum TipoInconveniente {
        CLIENTE_NO_ESTA,
        DIRECCION_INCORRECTA,
        ELECTRODOMESTICO_NO_ES_EL_REPORTADO,
        FALTA_HERRAMIENTA,
        RIESGO_SEGURIDAD,
        OTRO
    }

    public enum Estado {
        ABIERTO, REVISADO, RESUELTO
    }
}