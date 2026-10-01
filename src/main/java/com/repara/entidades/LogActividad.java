package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "logs_actividad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(name = "entidad_afectada", length = 50)
    private String entidadAfectada;

    @Column(name = "id_entidad")
    private Long idEntidad;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "fecha_accion", nullable = false, updatable = false)
    private LocalDateTime fechaAccion;

    @PrePersist
    protected void alCrear() {
        fechaAccion = LocalDateTime.now();
    }
}