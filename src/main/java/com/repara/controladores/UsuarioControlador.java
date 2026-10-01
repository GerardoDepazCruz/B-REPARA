package com.repara.controladores;

import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.dtos.respuesta.UsuarioRespuesta;
import com.repara.entidades.Usuario;
import com.repara.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ResponseEntity<ApiRespuesta<UsuarioRespuesta>> obtenerPerfil(Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        return ResponseEntity.ok(ApiRespuesta.exitoso(usuarioServicio.convertir(usuario)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<ApiRespuesta<UsuarioRespuesta>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiRespuesta.exitoso(usuarioServicio.obtenerRespuesta(id)));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los usuarios (Admin)")
    public ResponseEntity<ApiRespuesta<List<UsuarioRespuesta>>> listarTodos() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(usuarioServicio.listarTodos()));
    }

    @GetMapping("/clientes")
    @Operation(summary = "Listar solo clientes (Admin)")
    public ResponseEntity<ApiRespuesta<List<UsuarioRespuesta>>> listarClientes() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(usuarioServicio.listarClientes()));
    }
}