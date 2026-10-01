package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pagos_metodos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoMetodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo")
    private Integer idMetodo;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    private String descripcion;

    @Column(name = "icono_url")
    private String iconoUrl;

    @Builder.Default
    @Column(name = "comision_porcentaje", nullable = false)
    private BigDecimal comisionPorcentaje = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ACTIVO;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}