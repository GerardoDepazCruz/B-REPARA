package com.repara.utilidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaUtil {
    
    private static final DateTimeFormatter FORMATO_FECHA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public static String formatearFecha(LocalDateTime fecha) {
        return fecha != null ? fecha.format(FORMATO_FECHA) : "";
    }
    
    public static String formatearFechaHora(LocalDateTime fecha) {
        return fecha != null ? fecha.format(FORMATO_FECHA_HORA) : "";
    }
}