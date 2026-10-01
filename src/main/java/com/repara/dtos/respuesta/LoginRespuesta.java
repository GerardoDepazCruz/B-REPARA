package com.repara.dtos.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespuesta {
    
    private String token;
    private String tipoToken = "Bearer";
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;
    private Boolean esTecnico;
    private String fotoPerfilUrl;
}