package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "notificacion_destinatario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(NotificacionDestinatarioId.class)
public class NotificacionDestinatario {

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_notificacion")
    private Notificacion notificacion;

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @Builder.Default
    @Column(nullable = false)
    private Boolean leida = false;
}