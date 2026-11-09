package com.repara.dtos.respuesta;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TecnicoRankingDTO {
    private Long idTecnico;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private BigDecimal calificacionPromedio;
    private Integer totalServicios;
}