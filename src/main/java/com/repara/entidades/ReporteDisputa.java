package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reportes_disputas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDisputa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reportante", nullable = false)
    private Usuario reportante;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reportante", nullable = false)
    private TipoReportante tipoReportante = TipoReportante.CLIENTE;

    @Column(nullable = false)
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ABIERTO;

    private String resolucion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_resolutor")
    private Usuario adminResolutor;

    @Column(name = "fecha_reporte", nullable = false, updatable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @PrePersist
    protected void alCrear() {
        fechaReporte = LocalDateTime.now();
    }

    public enum TipoReportante {
        CLIENTE, TECNICO, ADMIN
    }

    public enum Estado {
        ABIERTO, EN_REVISION, CERRADO
    }
}