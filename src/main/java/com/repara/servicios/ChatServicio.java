package com.repara.servicios;

import com.repara.dtos.peticion.ChatPeticion;
import com.repara.entidades.ChatTrayecto;
import com.repara.entidades.SolicitudServicio;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.ChatTrayectoRepositorio;
import com.repara.repositorios.SolicitudServicioRepositorio;
import com.repara.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServicio {

    @Autowired
    private ChatTrayectoRepositorio chatRepositorio;

    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public ChatTrayecto enviarMensaje(Long idEmisor, ChatPeticion peticion) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(peticion.getIdSolicitud())
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        Usuario emisor = usuarioRepositorio.findById(idEmisor)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // Determinar receptor
        Long idReceptor = emisor.getIdUsuario().equals(solicitud.getCliente().getIdUsuario())
                ? solicitud.getTecnico().getUsuario().getIdUsuario()
                : solicitud.getCliente().getIdUsuario();

        Usuario receptor = usuarioRepositorio.findById(idReceptor)
                .orElseThrow(() -> new RecursoNoEncontradoException("Receptor no encontrado"));

        ChatTrayecto chat = ChatTrayecto.builder()
                .solicitud(solicitud)
                .emisor(emisor)
                .receptor(receptor)
                .mensaje(peticion.getMensaje())
                .tipoMensaje(ChatTrayecto.TipoMensaje.valueOf(peticion.getTipoMensaje()))
                .leido(false)
                .build();

        return chatRepositorio.save(chat);
    }

    public List<ChatTrayecto> listarMensajes(Long idSolicitud) {
        return chatRepositorio.findBySolicitudIdSolicitudOrderByFechaEnvioAsc(idSolicitud);
    }

    @Transactional
    public void marcarLeidos(Long idSolicitud, Long idUsuario) {
        List<ChatTrayecto> mensajes = chatRepositorio.findBySolicitudIdSolicitudAndLeidoFalse(idSolicitud);
        mensajes.stream()
                .filter(m -> m.getReceptor().getIdUsuario().equals(idUsuario))
                .forEach(m -> {
                    m.setLeido(true);
                    m.setFechaLectura(LocalDateTime.now());
                    chatRepositorio.save(m);
                });
    }
}