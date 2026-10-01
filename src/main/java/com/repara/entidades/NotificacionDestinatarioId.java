package com.repara.entidades;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDestinatarioId implements Serializable {
    private Long notificacion;
    private Long usuario;
}