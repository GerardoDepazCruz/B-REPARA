package com.repara.dtos.peticion;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagoPeticion {
    
    @NotNull(message = "El ID de método de pago es obligatorio")
    private Integer idMetodoPago;
    
    private String referenciaTransaccion;
    
    private String numeroOperacion;

    private BigDecimal monto;
}