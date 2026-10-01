package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "contrasena_hash", nullable = false)
    private String contrasenaHash;

    @Column(length = 20)
    private String telefono;

    @Column(unique = true, length = 15)
    private String dni;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Builder.Default
    @Column(name = "es_tecnico", nullable = false)
    private Boolean esTecnico = false;

    @Builder.Default
    @Column(name = "correo_verificado", nullable = false)
    private Boolean correoVerificado = false;

    @Column(name = "token_verificacion")
    private String tokenVerificacion;

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    @Builder.Default
    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "motivo_suspension")
    private String motivoSuspension;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void alCrear() {
        fechaRegistro = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void alActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Relaciones
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DireccionCliente> direcciones;

    public enum Rol {
        CLIENTE, ADMIN, TECNICO
    }

    public enum Estado {
        ACTIVO, SUSPENDIDO, INACTIVO
    }
}