package com.repara.controladores;

import com.repara.dtos.peticion.PostulacionPeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.entidades.PostulacionTecnico;
import com.repara.entidades.Usuario;
import com.repara.servicios.PostulacionServicio;
import com.repara.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postulaciones")
@Tag(name = "Postulaciones", description = "Gestión de postulaciones a técnico")
@CrossOrigin(origins = "*")
public class PostulacionControlador {

    @Autowired
    private PostulacionServicio postulacionServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/crear")
    @Operation(summary = "Cliente postula para ser técnico")
    public ResponseEntity<ApiRespuesta<PostulacionTecnico>> crear(
            Authentication auth, 
            @Valid @RequestBody PostulacionPeticion peticion) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        PostulacionTecnico postulacion = postulacionServicio.crearPostulacion(usuario.getIdUsuario(), peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Postulación enviada", postulacion));
    }

    @GetMapping("/mis-postulaciones")
    @Operation(summary = "Ver mis postulaciones")
    public ResponseEntity<ApiRespuesta<List<PostulacionTecnico>>> misPostulaciones(Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        return ResponseEntity.ok(ApiRespuesta.exitoso(
            postulacionServicio.listarPorUsuario(usuario.getIdUsuario())));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Listar postulaciones pendientes (Admin)")
    public ResponseEntity<ApiRespuesta<List<PostulacionTecnico>>> listarPendientes() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(postulacionServicio.listarPendientes()));
    }

    @GetMapping("/todas")
    @Operation(summary = "Listar todas las postulaciones (Admin)")
    public ResponseEntity<ApiRespuesta<List<PostulacionTecnico>>> listarTodas() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(postulacionServicio.listarTodas()));
    }

    @PostMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar postulación (Admin)")
    public ResponseEntity<ApiRespuesta<PostulacionTecnico>> aprobar(
            @PathVariable Long id, 
            Authentication auth) {
        Usuario admin = usuarioServicio.obtenerPorCorreo(auth.getName());
        PostulacionTecnico postulacion = postulacionServicio.aprobar(id, admin.getIdUsuario());
        return ResponseEntity.ok(ApiRespuesta.exitoso("Postulación aprobada", postulacion));
    }

    @PostMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar postulación (Admin)")
    public ResponseEntity<ApiRespuesta<PostulacionTecnico>> rechazar(
            @PathVariable Long id, 
            @RequestBody Map<String, String> body,
            Authentication auth) {
        Usuario admin = usuarioServicio.obtenerPorCorreo(auth.getName());
        String motivo = body.getOrDefault("motivo", "No cumple requisitos");
        PostulacionTecnico postulacion = postulacionServicio.rechazar(id, admin.getIdUsuario(), motivo);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Postulación rechazada", postulacion));
    }
}