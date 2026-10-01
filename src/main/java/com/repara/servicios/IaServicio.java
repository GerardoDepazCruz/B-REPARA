package com.repara.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IaServicio {

    @Autowired
    private GeminiServicio geminiServicio;

    public String procesarMensaje(String mensaje) {
        return geminiServicio.conversar(mensaje);
    }
}