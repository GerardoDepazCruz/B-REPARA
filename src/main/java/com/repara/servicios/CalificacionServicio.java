package com.repara.servicios;

import com.repara.entidades.Calificacion;
import com.repara.entidades.SolicitudServicio;
import com.repara.entidades.Tecnico;
import com.repara.entidades.Usuario;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.CalificacionRepositorio;
import com.repara.repositorios.SolicitudServicioRepositorio;
import com.repara.repositorios.TecnicoRepositorio;
import com.repara.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CalificacionServicio {

    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Transactional
    public Calificacion calificar(Long idSolicitud, Long idCliente, Integer puntuacion, String comentario) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));
        
        Usuario cliente = usuarioRepositorio.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        if (solicitud.getTecnico() == null) {
            throw new RuntimeException("La solicitud no tiene técnico asignado");
        }

        Usuario tecnicoUsuario = solicitud.getTecnico().getUsuario();

        Calificacion calificacion = Calificacion.builder()
                .solicitud(solicitud)
                .calificador(cliente)
                .calificado(tecnicoUsuario)
                .tipoCalificador(Calificacion.TipoCalificador.CLIENTE)
                .puntuacion(puntuacion.byteValue())
                .comentario(comentario)
                .fecha(LocalDateTime.now())
                .build();

        calificacionRepositorio.save(calificacion);

        // Actualizar el promedio del técnico
        Tecnico tecnico = solicitud.getTecnico();
        Double nuevoPromedio = calificacionRepositorio.calcularPromedioCalificaciones(tecnicoUsuario.getIdUsuario());
        if (nuevoPromedio != null) {
            tecnico.setCalificacionPromedio(new java.math.BigDecimal(nuevoPromedio.toString()));
            tecnicoRepositorio.save(tecnico);
        }

        return calificacion;
    }
}