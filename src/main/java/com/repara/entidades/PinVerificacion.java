package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pin_verificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PinVerificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pin")
    private Long idPin;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudServicio solicitud;

    @Column(name = "codigo_pin", nullable = false)
    private String codigoPin;

    @Builder.Default
    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Builder.Default
    @Column(name = "max_intentos", nullable = false)
    private Integer maxIntentos = 3;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "fecha_generacion", nullable = false, updatable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "fecha_uso")
    private LocalDateTime fechaUso;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico_verificador")
    private Tecnico tecnicoVerificador;

    @PrePersist
    protected void alCrear() {
        fechaGeneracion = LocalDateTime.now();
    }

    public enum Estado {
        ACTIVO, USADO, EXPIRADO, BLOQUEADO
    }
}