package com.repara.controladores;

import com.repara.dtos.peticion.InformePeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.entidades.InformeTecnico;
import com.repara.entidades.RepuestoUtilizado;
import com.repara.entidades.Tecnico;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.TecnicoRepositorio;
import com.repara.servicios.InformeServicio;
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
@RequestMapping("/informes")
@Tag(name = "Informes", description = "Informe final del técnico")
@CrossOrigin(origins = "*")
public class InformeControlador {

    @Autowired
    private InformeServicio informeServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @PostMapping("/crear")
    @Operation(summary = "Técnico crea el informe final")
    public ResponseEntity<ApiRespuesta<InformeTecnico>> crear(
            Authentication auth,
            @Valid @RequestBody InformePeticion peticion) {
        Usuario usuario = usuarioServicio.obtenerPorCorreo(auth.getName());
        Tecnico tecnico = tecnicoRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado"));
        InformeTecnico informe = informeServicio.crearInforme(tecnico.getIdTecnico(), peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Informe creado", informe));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Cliente: ver informe de una solicitud")
    public ResponseEntity<ApiRespuesta<InformeTecnico>> obtener(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(ApiRespuesta.exitoso(
            informeServicio.obtenerPorSolicitud(idSolicitud)));
    }

    @GetMapping("/{idInforme}/repuestos")
    @Operation(summary = "Ver repuestos utilizados en un informe")
    public ResponseEntity<ApiRespuesta<List<RepuestoUtilizado>>> repuestos(@PathVariable Long idInforme) {
        return ResponseEntity.ok(ApiRespuesta.exitoso(informeServicio.listarRepuestos(idInforme)));
    }
}