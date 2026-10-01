package com.repara.dtos.peticion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InformePeticion {
    
    @NotNull(message = "El ID de solicitud es obligatorio")
    private Long idSolicitud;
    
    @NotBlank(message = "El resumen del trabajo es obligatorio")
    private String resumenTrabajo;
    
    private String diagnosticoDetallado;
    
    private String accionesRealizadas;
    
    private Integer tiempoEstimadoMin;
    
    private Integer tiempoRealMin;
    
    private BigDecimal manoObraCosto;
    
    private String observaciones;
    
    private String recomendacionesCliente;
    
    private String estadoFinal = "REPARADO";
    
    private List<RepuestoPeticion> repuestos;
    
    @Data
    public static class RepuestoPeticion {
        private String nombreRepuesto;
        private String descripcion;
        private String marca;
        private String modelo;
        private Integer cantidad = 1;
        private BigDecimal costoUnitario;
        private String proveedor;
        private Integer garantiaMeses = 0;
    }
}