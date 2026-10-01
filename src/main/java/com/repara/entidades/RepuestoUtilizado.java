package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "repuestos_utilizados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoUtilizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto")
    private Long idRepuesto;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_informe", nullable = false)
    private InformeTecnico informe;

    @Column(name = "nombre_repuesto", nullable = false)
    private String nombreRepuesto;

    private String descripcion;

    private String marca;

    private String modelo;

    @Builder.Default
    @Column(nullable = false)
    private Integer cantidad = 1;

    @Column(name = "costo_unitario", nullable = false)
    private BigDecimal costoUnitario;

    @Column(name = "costo_total", nullable = false)
    private BigDecimal costoTotal;

    @Column(name = "foto_repuesto_url")
    private String fotoRepuestoUrl;

    private String proveedor;

    @Builder.Default
    @Column(name = "garantia_meses")
    private Integer garantiaMeses = 0;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void alCrear() {
        fechaRegistro = LocalDateTime.now();
    }
}