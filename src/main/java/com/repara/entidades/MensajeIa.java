package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mensajes_ia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeIa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emisor emisor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Builder.Default
    @Column(name = "orden_mensaje", nullable = false)
    private Integer ordenMensaje = 0;

    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void alCrear() {
        fechaEnvio = LocalDateTime.now();
    }

    public enum Emisor {
        CLIENTE, IA
    }
}