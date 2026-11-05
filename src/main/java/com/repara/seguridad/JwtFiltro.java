package com.repara.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Rutas que no necesitan token (login, registro)
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/autenticacion/") || requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Si no hay token en el header, dejamos pasar la petición
        final String cabeceraAuth = request.getHeader("Authorization");
        if (cabeceraAuth == null || !cabeceraAuth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Si hay token, lo validamos
        String jwt = cabeceraAuth.substring(7);
        String correo = null;
        try {
            correo = jwtUtil.extraerCorreo(jwt);
        } catch (Exception e) {
            logger.warn("Token JWT inválido: " + e.getMessage());
        }

        if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

                if (jwtUtil.validarToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                logger.warn("Error al autenticar usuario: " + e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}