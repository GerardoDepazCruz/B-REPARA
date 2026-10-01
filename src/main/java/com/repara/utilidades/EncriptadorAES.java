package com.repara.utilidades;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class EncriptadorAES {
    
    private static final String CLAVE = "ReparaAES256Key2026SuperSeguraXYZ";
    private static final String ALGORITMO = "AES";
    
    public static String encriptar(String texto) {
        try {
            SecretKeySpec llave = new SecretKeySpec(CLAVE.getBytes(StandardCharsets.UTF_8), ALGORITMO);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, llave);
            byte[] encriptado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encriptado);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }
    
    public static String desencriptar(String textoEncriptado) {
        try {
            SecretKeySpec llave = new SecretKeySpec(CLAVE.getBytes(StandardCharsets.UTF_8), ALGORITMO);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, llave);
            byte[] desencriptado = cipher.doFinal(Base64.getDecoder().decode(textoEncriptado));
            return new String(desencriptado, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }
}