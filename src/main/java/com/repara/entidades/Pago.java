package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metodo_pago")
    private PagoMetodo metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    private String descripcion;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "porcentaje_comision")
    private BigDecimal porcentajeComision;

    @Column(name = "monto_comision")
    private BigDecimal montoComision;

    @Column(name = "monto_neto_tecnico")
    private BigDecimal montoNetoTecnico;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPagoTexto;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @Column(name = "referencia_transaccion")
    private String referenciaTransaccion;

    @Column(name = "numero_operacion")
    private String numeroOperacion;

    @Column(name = "comprobante_url")
    private String comprobanteUrl;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void alCrear() {
        fechaCreacion = LocalDateTime.now();
    }

    public enum TipoPago {
        INICIAL, COMPLEMENTARIO
    }

    public enum Estado {
        PENDIENTE, PAGADO, REEMBOLSADO, FALLIDO
    }
}