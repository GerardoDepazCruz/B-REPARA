package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_emisor", nullable = false)
    private Usuario adminEmisor;

    @Column(nullable = false, length = 150)
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_destinatario", nullable = false)
    private TipoDestinatario tipoDestinatario;

    @Column(name = "programada_envio")
    private LocalDateTime programadaEnvio;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enviada = false;

    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void alCrear() {
        fechaEnvio = LocalDateTime.now();
    }

    public enum TipoDestinatario {
        TODOS_CLIENTES, TODOS_TECNICOS, TODOS, ESPECIFICO
    }
}