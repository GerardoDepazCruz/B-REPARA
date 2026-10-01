package com.repara.controladores;

import com.repara.dtos.peticion.PagoPeticion;
import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.entidades.Pago;
import com.repara.entidades.PagoMetodo;
import com.repara.servicios.PagoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "Pagos fraccionados (inicial y final)")
@CrossOrigin(origins = "*")
public class PagoControlador {

    @Autowired
    private PagoServicio pagoServicio;

    @GetMapping("/metodos")
    @Operation(summary = "Listar métodos de pago disponibles")
    public ResponseEntity<ApiRespuesta<List<PagoMetodo>>> metodos() {
        return ResponseEntity.ok(ApiRespuesta.exitoso(pagoServicio.listarMetodosActivos()));
    }

    @PostMapping("/inicial/{idSolicitud}")
    @Operation(summary = "Cliente paga monto inicial")
    public ResponseEntity<ApiRespuesta<Pago>> pagoInicial(
            @PathVariable Long idSolicitud,
            @Valid @RequestBody PagoPeticion peticion) {
        Pago pago = pagoServicio.registrarPagoInicial(idSolicitud, peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Pago inicial registrado", pago));
    }

    @PostMapping("/final/{idSolicitud}")
    @Operation(summary = "Cliente paga monto final")
    public ResponseEntity<ApiRespuesta<Pago>> pagoFinal(
            @PathVariable Long idSolicitud,
            @Valid @RequestBody PagoPeticion peticion) {
        Pago pago = pagoServicio.registrarPagoFinal(idSolicitud, peticion);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Pago final registrado", pago));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Ver pagos de una solicitud")
    public ResponseEntity<ApiRespuesta<List<Pago>>> listarPorSolicitud(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(ApiRespuesta.exitoso(pagoServicio.listarPorSolicitud(idSolicitud)));
    }
}