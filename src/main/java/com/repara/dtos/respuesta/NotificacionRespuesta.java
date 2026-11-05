package com.repara.dtos.respuesta;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionRespuesta {
    private Long idNotificacion;
    private String asunto;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fechaEnvio;
}