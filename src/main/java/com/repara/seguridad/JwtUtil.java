package com.repara.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secreto}")
    private String secreto;

    @Value("${jwt.expiracion}")
    private Long expiracion;

    private SecretKey getClaveFirma() {
        return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String correo, Long idUsuario, String rol, Boolean esTecnico) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idUsuario", idUsuario);
        claims.put("rol", rol);
        claims.put("esTecnico", esTecnico);
        return crearToken(claims, correo);
    }

    private String crearToken(Map<String, Object> claims, String subject) {
        Date ahora = new Date();
        Date expiracionFecha = new Date(ahora.getTime() + expiracion);

        // ✅ API nueva de JJWT 0.12+
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(ahora)
                .expiration(expiracionFecha)
                .signWith(getClaveFirma())
                .compact();
    }

    public String extraerCorreo(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public Long extraerIdUsuario(String token) {
        return extraerClaim(token, claims -> claims.get("idUsuario", Long.class));
    }

    public String extraerRol(String token) {
        return extraerClaim(token, claims -> claims.get("rol", String.class));
    }

    public Boolean extraerEsTecnico(String token) {
        return extraerClaim(token, claims -> claims.get("esTecnico", Boolean.class));
    }

    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        // ✅ API nueva de JJWT 0.12+
        return Jwts.parser()
                .verifyWith(getClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean tokenExpirado(String token) {
        try {
            return extraerExpiracion(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public Boolean validarToken(String token, String correo) {
        try {
            final String correoToken = extraerCorreo(token);
            return correoToken.equals(correo) && !tokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }
}