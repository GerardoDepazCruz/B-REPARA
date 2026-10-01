package com.repara.controladores;

import com.repara.dtos.peticion.ChatPeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.entidades.ChatTrayecto;
import com.repara.entidades.Usuario;
import com.repara.servicios.ChatServicio;
import com.repara.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat", description = "Chat en trayecto cliente-técnico")
@CrossOrigin(origins = "*")
public class ChatControlador {

    @Autowired
    private ChatServicio chatServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/enviar")
    @Operation(summary = "Enviar mensaje en el chat")
    public ResponseEntity<ApiRespuesta<ChatTrayecto>> enviar(
            Authentication auth,
            @Valid @RequestBody ChatPeticion peticion) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        ChatTrayecto mensaje = chatServicio.enviarMensaje(usuario.getIdUsuario(), peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Mensaje enviado", mensaje));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Listar mensajes de una solicitud")
    public ResponseEntity<ApiRespuesta<List<ChatTrayecto>>> listar(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(ApiRespuesta.exitoso(chatServicio.listarMensajes(idSolicitud)));
    }

    @PostMapping("/solicitud/{idSolicitud}/marcar-leidos")
    @Operation(summary = "Marcar mensajes como leídos")
    public ResponseEntity<ApiRespuesta<Void>> marcarLeidos(
            @PathVariable Long idSolicitud,
            Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        chatServicio.marcarLeidos(idSolicitud, usuario.getIdUsuario());
        return ResponseEntity.ok(ApiRespuesta.exitoso("Mensajes marcados como leídos", null));
    }
}