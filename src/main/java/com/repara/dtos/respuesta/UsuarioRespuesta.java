package com.repara.dtos.respuesta;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioRespuesta {
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String dni;
    private String fotoPerfilUrl;
    private String rol;
    private Boolean esTecnico;
    private String estado;
    private Boolean correoVerificado;
    private LocalDateTime fechaRegistro;
}