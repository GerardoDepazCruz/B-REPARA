package com.repara.dtos.peticion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginPeticion {
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String correo;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}