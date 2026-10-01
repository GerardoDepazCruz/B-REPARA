package com.repara.dtos.peticion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PinPeticion {
    
    @NotNull(message = "El ID de solicitud es obligatorio")
    private Long idSolicitud;
    
    @NotBlank(message = "El PIN es obligatorio")
    private String pin;
}