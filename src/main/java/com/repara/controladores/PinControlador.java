package com.repara.controladores;

import com.repara.dtos.peticion.PinPeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.entidades.PinVerificacion;
import com.repara.entidades.Tecnico;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.TecnicoRepositorio;
import com.repara.servicios.PinServicio;
import com.repara.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pin")
@Tag(name = "PIN", description = "Verificación con PIN de 4 dígitos")
@CrossOrigin(origins = "*")
public class PinControlador {

    @Autowired
    private PinServicio pinServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @PostMapping("/generar/{idSolicitud}")
    @Operation(summary = "Generar PIN para una solicitud")
    public ResponseEntity<ApiRespuesta<PinVerificacion>> generar(@PathVariable Long idSolicitud) {
        PinVerificacion pin = pinServicio.generarPin(idSolicitud);
        return ResponseEntity.ok(ApiRespuesta.exitoso("PIN generado", pin));
    }

    @PostMapping("/verificar")
    @Operation(summary = "Técnico ingresa el PIN proporcionado por el cliente")
    public ResponseEntity<ApiRespuesta<Boolean>> verificar(
            @Valid @RequestBody PinPeticion peticion,
            Authentication auth) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        Tecnico tecnico = tecnicoRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado"));
        
        boolean valido = pinServicio.verificarPin(
                peticion.getIdSolicitud(), 
                peticion.getPin(), 
                tecnico.getIdTecnico());
        return ResponseEntity.ok(ApiRespuesta.exitoso("PIN verificado", valido));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Obtener PIN activo de una solicitud (Cliente)")
    public ResponseEntity<ApiRespuesta<PinVerificacion>> obtener(@PathVariable Long idSolicitud) {
        PinVerificacion pin = pinServicio.obtenerPinActivo(idSolicitud);
        return ResponseEntity.ok(ApiRespuesta.exitoso(pin));
    }
}