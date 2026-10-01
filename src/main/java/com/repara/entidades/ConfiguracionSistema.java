package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_sistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_config")
    private Integer idConfig;

    @Column(nullable = false, unique = true, length = 80)
    private String clave;

    @Column(nullable = false)
    private String valor;

    private String descripcion;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dato")
    private TipoDato tipoDato = TipoDato.STRING;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PreUpdate
    protected void alActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum TipoDato {
        STRING, INT, DECIMAL, BOOLEAN, JSON
    }
}