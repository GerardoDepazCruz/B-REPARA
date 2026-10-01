package com.repara.dtos.respuesta;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SolicitudRespuesta {
    private Long idSolicitud;
    private String estado;
    private String descripcionProblema;
    private String direccionServicio;
    private String resumenIa;
    private BigDecimal costoEstimado;
    private BigDecimal costoFinal;
    private String nombreCliente;
    private String nombreTecnico;
    private String nombreCategoria;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaAceptacion;
    private LocalDateTime fechaFinalizacion;
}