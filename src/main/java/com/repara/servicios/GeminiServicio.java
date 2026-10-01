package com.repara.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GeminiServicio {

    @Autowired
    private WebClient geminiWebClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public String conversar(String mensajeUsuario) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", construirPrompt(mensajeUsuario))
                            ))
                    )
            );

            String respuesta = geminiWebClient.post()
                    // ✅ CAMBIO CLAVE: Ya NO usamos ?key= en la URL
                    // Ahora usamos el header x-goog-api-key
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::extraerTextoRespuesta)
                    .onErrorResume(e -> {
                        System.err.println("ERROR GEMINI: " + e.getMessage());
                        e.printStackTrace();
                        return Mono.just("Error al conectar con Gemini: " + e.getMessage());
                    })
                    .block();

            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectar con la IA: " + e.getMessage();
        }
    }

    private String construirPrompt(String mensaje) {
        return "Eres un asistente técnico de RePara, plataforma de servicios de reparación de " +
               "electrodomésticos a domicilio en Lima, Perú. Tu trabajo es ayudar al cliente a " +
               "describir su problema para generar una solicitud de servicio.\n\n" +
               "Debes solicitar de forma amable: ubicación, tipo de electrodoméstico, y una breve " +
               "descripción del problema. Al final, haz un resumen del caso.\n\n" +
               "Mensaje del cliente: " + mensaje;
    }

    @SuppressWarnings("unchecked")
    private String extraerTextoRespuesta(Map<String, Object> respuesta) {
        try {
            List<Map<String, Object>> candidatos = (List<Map<String, Object>>) respuesta.get("candidates");
            Map<String, Object> primerCandidato = candidatos.get(0);
            Map<String, Object> contenido = (Map<String, Object>) primerCandidato.get("content");
            List<Map<String, Object>> partes = (List<Map<String, Object>>) contenido.get("parts");
            return (String) partes.get(0).get("text");
        } catch (Exception e) {
            System.err.println("ERROR al parsear respuesta: " + respuesta);
            return "No se pudo interpretar la respuesta de la IA. Raw: " + respuesta;
        }
    }
}