package com.repara.dtos.peticion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SolicitudPeticion {
    
    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcionProblema;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccionServicio;
    
    private BigDecimal latitud;
    
    private BigDecimal longitud;
    
    private Long idDireccionCliente;
}