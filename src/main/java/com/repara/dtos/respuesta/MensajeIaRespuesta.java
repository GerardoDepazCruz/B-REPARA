package com.repara.dtos.respuesta;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MensajeIaRespuesta {
    private Long idMensaje;
    private String emisor;
    private String mensaje;
    private Integer ordenMensaje;
    private LocalDateTime fechaEnvio;
}