package com.repara.dtos.respuesta;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostulacionRespuesta {
    private Long idPostulacion;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String dni;
    
    private String especialidadDeclarada;
    private Integer aniosExperiencia;
    private String cvUrl;
    private String certificadosUrl;
    private String dniDocumentoUrl;
    private String estado;
    private LocalDateTime fechaPostulacion;
}