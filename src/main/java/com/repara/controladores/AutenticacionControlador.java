package com.repara.controladores;

import com.repara.dtos.peticion.LoginPeticion;
import com.repara.dtos.peticion.RegistroPeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.dtos.respuesta.LoginRespuesta;
import com.repara.entidades.Usuario;
import com.repara.servicios.AutenticacionServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacion")
@Tag(name = "Autenticación", description = "Endpoints de registro y login")
@CrossOrigin(origins = "*")
public class AutenticacionControlador {

    @Autowired
    private AutenticacionServicio autenticacionServicio;

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo cliente")
    public ResponseEntity<ApiRespuesta<Usuario>> registrar(@Valid @RequestBody RegistroPeticion peticion) {
        Usuario usuario = autenticacionServicio.registrar(peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Usuario registrado exitosamente", usuario));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión (Cliente, Técnico o Admin)")
    public ResponseEntity<ApiRespuesta<LoginRespuesta>> login(@Valid @RequestBody LoginPeticion peticion) {
        LoginRespuesta respuesta = autenticacionServicio.login(peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Login exitoso", respuesta));
    }
}