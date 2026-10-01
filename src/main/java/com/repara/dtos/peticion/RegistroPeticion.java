package com.repara.dtos.peticion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroPeticion {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String correo;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
    
    private String telefono;
    
    private String dni;
}