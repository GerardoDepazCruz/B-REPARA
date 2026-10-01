package com.repara.controladores;

import com.repara.dtos.peticion.SolicitudPeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.entidades.SolicitudServicio;
import com.repara.entidades.Tecnico;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.TecnicoRepositorio;
import com.repara.servicios.SolicitudServicioService;
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
@RequestMapping("/solicitudes")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de servicio")
@CrossOrigin(origins = "*")
public class SolicitudControlador {

    @Autowired
    private SolicitudServicioService solicitudServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @PostMapping("/crear")
    @Operation(summary = "Cliente crea solicitud de servicio")
    public ResponseEntity<ApiRespuesta<SolicitudServicio>> crear(
            Authentication auth,
            @Valid @RequestBody SolicitudPeticion peticion) {
        Usuario cliente = usuarioServicio.obtenerPorCorreo(auth.getName());
        SolicitudServicio solicitud = solicitudServicio.crear(cliente.getIdUsuario(), peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Solicitud creada", solicitud));
    }

    @GetMapping("/mis-solicitudes")
    @Operation(summary = "Cliente: mis solicitudes")
    public ResponseEntity<ApiRespuesta<List<SolicitudServicio>>> misSolicitudes(Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        return ResponseEntity.ok(ApiRespuesta.exitoso(
                solicitudServicio.listarPorCliente(usuario.getIdUsuario())));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Técnico: solicitudes pendientes de asignación")
    public ResponseEntity<ApiRespuesta<List<SolicitudServicio>>> pendientes() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(solicitudServicio.listarPendientes()));
    }

    @PostMapping("/{id}/aceptar")
    @Operation(summary = "Técnico acepta solicitud")
    public ResponseEntity<ApiRespuesta<SolicitudServicio>> aceptar(
            @PathVariable Long id,
            Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        Tecnico tecnico = tecnicoRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado"));
        SolicitudServicio solicitud = solicitudServicio.aceptar(id, tecnico.getIdTecnico());
        return ResponseEntity.ok(ApiRespuesta.exitoso("Solicitud aceptada", solicitud));
    }

    @PostMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de solicitud")
    public ResponseEntity<ApiRespuesta<SolicitudServicio>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        SolicitudServicio.Estado nuevoEstado = SolicitudServicio.Estado.valueOf(body.get("estado"));
        String comentario = body.getOrDefault("comentario", "");
        SolicitudServicio solicitud = solicitudServicio.cambiarEstado(
                id, nuevoEstado, usuario.getIdUsuario(), comentario);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Estado actualizado", solicitud));
    }
}