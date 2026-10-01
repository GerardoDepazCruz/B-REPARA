package com.repara.controladores;

import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.dtos.respuesta.UsuarioRespuesta;
import com.repara.entidades.Notificacion;
import com.repara.entidades.Pago;
import com.repara.entidades.SolicitudServicio;
import com.repara.entidades.Usuario;
import com.repara.repositorios.PagoRepositorio;
import com.repara.repositorios.SolicitudServicioRepositorio;
import com.repara.repositorios.UsuarioRepositorio;
import com.repara.servicios.NotificacionServicio;
import com.repara.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Panel de administración")
@CrossOrigin(origins = "*")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PagoRepositorio pagoRepositorio;

    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Autowired
    private NotificacionServicio notificacionServicio;

    @GetMapping("/dashboard")
    @Operation(summary = "Métricas del dashboard")
    public ResponseEntity<ApiRespuesta<Map<String, Object>>> dashboard() {
        Map<String, Object> datos = new HashMap<>();
        
        // Contadores
        datos.put("totalUsuarios", usuarioRepositorio.count());
        datos.put("totalClientes", usuarioRepositorio.findByRol(Usuario.Rol.CLIENTE).size());
        datos.put("totalTecnicos", usuarioRepositorio.findByEsTecnicoTrue().size());
        
        // Solicitudes por estado
        datos.put("solicitudesPendientes", 
            solicitudRepositorio.countByEstado(SolicitudServicio.Estado.PENDIENTE_ASIGNACION));
        datos.put("solicitudesCompletadas", 
            solicitudRepositorio.countByEstado(SolicitudServicio.Estado.COMPLETADO));
        
        // Financiero
        BigDecimal totalBruto = pagoRepositorio.sumTotalPagado();
        BigDecimal totalComisiones = pagoRepositorio.sumTotalComisiones();
        datos.put("balanceTotal", totalBruto);
        datos.put("gananciasNetas", totalComisiones);
        
        return ResponseEntity.ok(ApiRespuesta.exitoso(datos));
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<ApiRespuesta<List<UsuarioRespuesta>>> usuarios() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(usuarioServicio.listarTodos()));
    }

    @PostMapping("/usuarios/{id}/suspender")
    @Operation(summary = "Suspender un usuario")
    public ResponseEntity<ApiRespuesta<Void>> suspender(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Usuario usuario = usuarioRepositorio.findById(id).orElseThrow();
        usuario.setEstado(Usuario.Estado.SUSPENDIDO);
        usuario.setMotivoSuspension(body.getOrDefault("motivo", "Sin motivo especificado"));
        usuarioRepositorio.save(usuario);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Usuario suspendido", null));
    }

    @PostMapping("/notificar")
    @Operation(summary = "Enviar notificación masiva")
    public ResponseEntity<ApiRespuesta<Notificacion>> notificar(
            Authentication auth,
            @RequestBody Map<String, String> body) {
        Usuario admin = usuarioServicio.obtenerPorCorreo(auth.getName());
        String asunto = body.get("asunto");
        String mensaje = body.get("mensaje");
        Notificacion.TipoDestinatario tipo = 
            Notificacion.TipoDestinatario.valueOf(body.getOrDefault("tipo", "TODOS"));
        
        Notificacion notificacion = notificacionServicio.crearNotificacionMasiva(
            admin.getIdUsuario(), asunto, mensaje, tipo);
        
        return ResponseEntity.ok(ApiRespuesta.exitoso("Notificación enviada", notificacion));
    }

    @GetMapping("/pagos")
    @Operation(summary = "Ver todos los pagos")
    public ResponseEntity<ApiRespuesta<List<Pago>>> pagos() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(pagoRepositorio.findAll()));
    }
}