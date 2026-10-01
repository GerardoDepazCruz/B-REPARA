package com.repara.servicios;

import com.repara.dtos.respuesta.UsuarioRespuesta;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario obtenerPorId(Long idUsuario) {
        return usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }

    public UsuarioRespuesta obtenerRespuesta(Long idUsuario) {
        return convertir(obtenerPorId(idUsuario));
    }

    public List<UsuarioRespuesta> listarClientes() {
        return usuarioRepositorio.findByRol(Usuario.Rol.CLIENTE)
                .stream().map(this::convertir).collect(Collectors.toList());
    }

    public List<UsuarioRespuesta> listarTodos() {
        return usuarioRepositorio.findAll()
                .stream().map(this::convertir).collect(Collectors.toList());
    }

    public Usuario obtenerPorCorreo(String correo) {
    return usuarioRepositorio.findByCorreo(correo)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
}

public UsuarioRespuesta convertir(Usuario u) {
    UsuarioRespuesta r = new UsuarioRespuesta();
    r.setIdUsuario(u.getIdUsuario());
    r.setNombres(u.getNombres());
    r.setApellidos(u.getApellidos());
    r.setCorreo(u.getCorreo());
    r.setTelefono(u.getTelefono());
    r.setDni(u.getDni());
    r.setFotoPerfilUrl(u.getFotoPerfilUrl());
    r.setRol(u.getRol().name());
    r.setEsTecnico(u.getEsTecnico());
    r.setEstado(u.getEstado().name());
    r.setCorreoVerificado(u.getCorreoVerificado());
    r.setFechaRegistro(u.getFechaRegistro());
    return r;
}

    
}