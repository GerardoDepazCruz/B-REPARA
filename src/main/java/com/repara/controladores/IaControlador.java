package com.repara.controladores;

import com.repara.dtos.respuesta.ApiRespuesta;
import com.repara.servicios.GeminiServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ia")
@Tag(name = "IA", description = "Asistente conversacional con Gemini")
@CrossOrigin(origins = "*")
public class IaControlador {

    @Autowired
    private GeminiServicio geminiServicio;

    @PostMapping("/chat")
    @Operation(summary = "Enviar mensaje al asistente IA")
    public ResponseEntity<ApiRespuesta<Map<String, String>>> chat(@RequestBody Map<String, String> body) {
        String mensaje = body.getOrDefault("mensaje", "");
        if (mensaje.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiRespuesta.error("El mensaje no puede estar vacío"));
        }
        String respuesta = geminiServicio.conversar(mensaje);
        return ResponseEntity.ok(ApiRespuesta.exitoso("Respuesta generada", 
            Map.of("respuesta", respuesta)));
    }
}