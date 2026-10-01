package com.repara.dtos.peticion;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostulacionPeticion {
    
    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidadDeclarada;
    
    private Integer aniosExperiencia;
    
    private String cvUrl;
    
    private String certificadosUrl;
    
    private String dniDocumentoUrl;
}