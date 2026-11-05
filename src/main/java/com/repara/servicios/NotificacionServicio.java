package com.repara.servicios;

import com.repara.dtos.respuesta.NotificacionRespuesta;
import com.repara.entidades.Notificacion;
import com.repara.entidades.NotificacionDestinatario;
import com.repara.entidades.NotificacionDestinatarioId;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.NotificacionDestinatarioRepositorio;
import com.repara.repositorios.NotificacionRepositorio;
import com.repara.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionServicio {

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    @Autowired
    private NotificacionDestinatarioRepositorio destinatarioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public Notificacion crearNotificacionMasiva(Long idAdmin, String asunto, String mensaje, 
                                                  Notificacion.TipoDestinatario tipo) {
        Usuario admin = usuarioRepositorio.findById(idAdmin)
                .orElseThrow(() -> new RecursoNoEncontradoException("Admin no encontrado"));

        Notificacion notificacion = Notificacion.builder()
                .adminEmisor(admin)
                .asunto(asunto)
                .mensaje(mensaje)
                .tipoDestinatario(tipo)
                .enviada(true)
                .build();

        notificacionRepositorio.save(notificacion);

        List<Usuario> destinatarios;
        if (tipo == Notificacion.TipoDestinatario.TODOS_CLIENTES) {
            destinatarios = usuarioRepositorio.findByRol(Usuario.Rol.CLIENTE);
        } else if (tipo == Notificacion.TipoDestinatario.TODOS_TECNICOS) {
            destinatarios = usuarioRepositorio.findByEsTecnicoTrue();
        } else {
            destinatarios = usuarioRepositorio.findAll();
        }

        for (Usuario u : destinatarios) {
            NotificacionDestinatario nd = NotificacionDestinatario.builder()
                    .notificacion(notificacion)
                    .usuario(u)
                    .leida(false)
                    .build();
            destinatarioRepositorio.save(nd);
        }

        return notificacion;
    }

    public List<NotificacionDestinatario> listarPorUsuario(Long idUsuario) {
        return destinatarioRepositorio.findByUsuarioIdUsuario(idUsuario);
    }

    // ✅ CORREGIDO: Usa NotificacionDestinatarioId (clase separada)
    @Transactional
    public void marcarLeida(Long idNotificacion, Long idUsuario) {
        NotificacionDestinatarioId id = new NotificacionDestinatarioId(idNotificacion, idUsuario);
        destinatarioRepositorio.findById(id).ifPresent(nd -> {
            nd.setLeida(true);
            nd.setFechaLectura(LocalDateTime.now());
            destinatarioRepositorio.save(nd);
        });
    }

    public List<Notificacion> listarTodas() {
        return notificacionRepositorio.findAll();
    }

        public List<NotificacionRespuesta> listarRespuestasPorUsuario(Long idUsuario) {
        List<NotificacionDestinatario> lista = destinatarioRepositorio.findByUsuarioIdUsuario(idUsuario);
        return lista.stream().map(nd -> {
            NotificacionRespuesta dto = new NotificacionRespuesta();
            // Usamos el ID de la notificación para el DTO
            dto.setIdNotificacion(nd.getNotificacion().getIdNotificacion());
            dto.setAsunto(nd.getNotificacion().getAsunto());
            dto.setMensaje(nd.getNotificacion().getMensaje());
            dto.setLeida(nd.getLeida());
            dto.setFechaEnvio(nd.getNotificacion().getFechaEnvio());
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }
}