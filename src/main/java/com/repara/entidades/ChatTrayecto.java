package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "chat_trayecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatTrayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emisor", nullable = false)
    private Usuario emisor;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receptor", nullable = false)
    private Usuario receptor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mensaje", nullable = false)
    private TipoMensaje tipoMensaje = TipoMensaje.TEXTO;

    @Builder.Default
    @Column(nullable = false)
    private Boolean leido = false;

    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @PrePersist
    protected void alCrear() {
        fechaEnvio = LocalDateTime.now();
    }

    public enum TipoMensaje {
        TEXTO, UBICACION, IMAGEN
    }
}