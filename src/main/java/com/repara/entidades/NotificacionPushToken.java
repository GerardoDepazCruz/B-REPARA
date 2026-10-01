package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "notificaciones_push_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionPushToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long idToken;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "token_fcm", nullable = false, unique = true)
    private String tokenFcm;

    private String dispositivo;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Plataforma plataforma = Plataforma.ANDROID;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_ultimo_uso")
    private LocalDateTime fechaUltimoUso;

    @PrePersist
    protected void alCrear() {
        fechaRegistro = LocalDateTime.now();
    }

    public enum Plataforma {
        ANDROID, IOS, WEB
    }
}