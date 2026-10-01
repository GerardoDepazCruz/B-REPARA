package com.repara.dtos.respuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRespuesta<T> {
    
    private boolean exito;
    private String mensaje;
    private T datos;
    
    public static <T> ApiRespuesta<T> exitoso(String mensaje, T datos) {
        return new ApiRespuesta<>(true, mensaje, datos);
    }
    
    public static <T> ApiRespuesta<T> exitoso(T datos) {
        return new ApiRespuesta<>(true, "Operación exitosa", datos);
    }
    
    public static <T> ApiRespuesta<T> error(String mensaje) {
        return new ApiRespuesta<>(false, mensaje, null);
    }
}