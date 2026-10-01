package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidades_solicitud")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(EspecialidadSolicitudId.class)
public class EspecialidadSolicitud {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud")
    private SolicitudServicio solicitud;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Builder.Default
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;
}