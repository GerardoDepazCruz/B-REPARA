package com.repara.utilidades;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class PinGenerador {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static String generarPin4Digitos() {
        int pin = 1000 + RANDOM.nextInt(9000);
        return String.valueOf(pin);
    }
}