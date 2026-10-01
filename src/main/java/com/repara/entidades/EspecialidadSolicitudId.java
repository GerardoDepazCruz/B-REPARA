package com.repara.entidades;

import lombok.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadSolicitudId implements Serializable {
    @JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "id_solicitud")
private SolicitudServicio solicitud;
    @JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "id_categoria")
private Categoria categoria;

    
}