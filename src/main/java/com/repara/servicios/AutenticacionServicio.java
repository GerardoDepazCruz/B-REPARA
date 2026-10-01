package com.repara.servicios;

import com.repara.dtos.peticion.LoginPeticion;
import com.repara.dtos.peticion.RegistroPeticion;
import com.repara.dtos.respuesta.LoginRespuesta;
import com.repara.entidades.Usuario;
import com.repara.excepciones.ReglaNegocioException;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.UsuarioRepositorio;
import com.repara.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AutenticacionServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Usuario registrar(RegistroPeticion peticion) {
        if (usuarioRepositorio.existsByCorreo(peticion.getCorreo())) {
            throw new ReglaNegocioException("Ya existe un usuario con ese correo");
        }
        if (peticion.getDni() != null && usuarioRepositorio.existsByDni(peticion.getDni())) {
            throw new ReglaNegocioException("Ya existe un usuario con ese DNI");
        }

        Usuario usuario = Usuario.builder()
                .nombres(peticion.getNombres())
                .apellidos(peticion.getApellidos())
                .correo(peticion.getCorreo())
                .contrasenaHash(passwordEncoder.encode(peticion.getContrasena()))
                .telefono(peticion.getTelefono())
                .dni(peticion.getDni())
                .rol(Usuario.Rol.CLIENTE)
                .esTecnico(false)
                .correoVerificado(true) // Simulado
                .tokenVerificacion(UUID.randomUUID().toString())
                .estado(Usuario.Estado.ACTIVO)
                .build();

        return usuarioRepositorio.save(usuario);
    }

    @Transactional
    public LoginRespuesta login(LoginPeticion peticion) {
        Usuario usuario = usuarioRepositorio.findByCorreo(peticion.getCorreo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Credenciales inválidas"));

        if (usuario.getEstado() == Usuario.Estado.SUSPENDIDO) {
            throw new ReglaNegocioException("Tu cuenta está suspendida. Contacta al administrador");
        }

        if (!passwordEncoder.matches(peticion.getContrasena(), usuario.getContrasenaHash())) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            if (usuario.getIntentosFallidos() >= 5) {
                usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(15));
            }
            usuarioRepositorio.save(usuario);
            throw new ReglaNegocioException("Credenciales inválidas");
        }

        // Reset de intentos fallidos
        usuario.setIntentosFallidos(0);
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepositorio.save(usuario);

        String token = jwtUtil.generarToken(
                usuario.getCorreo(),
                usuario.getIdUsuario(),
                usuario.getRol().name(),
                usuario.getEsTecnico()
        );

        return LoginRespuesta.builder()
                .token(token)
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .esTecnico(usuario.getEsTecnico())
                .fotoPerfilUrl(usuario.getFotoPerfilUrl())
                .build();
    }
}