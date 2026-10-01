package com.repara.servicios;

import com.repara.dtos.peticion.SolicitudPeticion;
import com.repara.entidades.*;
import com.repara.excepciones.ReglaNegocioException;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudServicioService {

    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Autowired
    private PinServicio pinServicio;

    @Autowired
    private HistorialEstadoSolicitudRepositorio historialRepositorio;

    @Transactional
    public SolicitudServicio crear(Long idCliente, SolicitudPeticion peticion) {
        Usuario cliente = usuarioRepositorio.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        Categoria categoria = categoriaRepositorio.findById(peticion.getIdCategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));

        SolicitudServicio solicitud = SolicitudServicio.builder()
                .cliente(cliente)
                .categoria(categoria)
                .descripcionProblema(peticion.getDescripcionProblema())
                .direccionServicio(peticion.getDireccionServicio())
                .latitud(peticion.getLatitud())
                .longitud(peticion.getLongitud())
                .estado(SolicitudServicio.Estado.PENDIENTE_ASIGNACION)
                .build();

        solicitud = solicitudRepositorio.save(solicitud);
        registrarHistorial(solicitud, null, "PENDIENTE_ASIGNACION", cliente, "Solicitud creada");
        return solicitud;
    }

    public List<SolicitudServicio> listarPorCliente(Long idCliente) {
        return solicitudRepositorio.findByClienteIdUsuarioOrderByFechaSolicitudDesc(idCliente);
    }

    public List<SolicitudServicio> listarPorTecnico(Long idTecnico) {
        return solicitudRepositorio.findByTecnicoIdTecnicoOrderByFechaSolicitudDesc(idTecnico);
    }

    public List<SolicitudServicio> listarPendientes() {
        return solicitudRepositorio.findPendientesAsignacion();
    }

    @Transactional
    public SolicitudServicio aceptar(Long idSolicitud, Long idTecnico) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        if (solicitud.getEstado() != SolicitudServicio.Estado.PENDIENTE_ASIGNACION) {
            throw new ReglaNegocioException("La solicitud ya fue asignada");
        }

        Tecnico tecnico = tecnicoRepositorio.findById(idTecnico)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado"));

        solicitud.setTecnico(tecnico);
        solicitud.setEstado(SolicitudServicio.Estado.ACEPTADA);
        solicitud.setFechaAceptacion(LocalDateTime.now());
        solicitudRepositorio.save(solicitud);

        // Generar PIN automáticamente
        pinServicio.generarPin(idSolicitud);

        registrarHistorial(solicitud, "PENDIENTE_ASIGNACION", "ACEPTADA",
                tecnico.getUsuario(), "Aceptada por técnico");

        return solicitud;
    }

    @Transactional
    public SolicitudServicio cambiarEstado(Long idSolicitud, SolicitudServicio.Estado nuevoEstado,
                                            Long idUsuario, String comentario) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        String estadoAnterior = solicitud.getEstado().name();
        solicitud.setEstado(nuevoEstado);

        if (nuevoEstado == SolicitudServicio.Estado.COMPLETADO) {
            solicitud.setFechaFinalizacion(LocalDateTime.now());
        }

        solicitudRepositorio.save(solicitud);

        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        registrarHistorial(solicitud, estadoAnterior, nuevoEstado.name(), usuario, comentario);
        return solicitud;
    }

    private void registrarHistorial(SolicitudServicio solicitud, String anterior, String nuevo,
                                     Usuario usuario, String comentario) {
        HistorialEstadoSolicitud historial = HistorialEstadoSolicitud.builder()
                .solicitud(solicitud)
                .estadoAnterior(anterior)
                .estadoNuevo(nuevo)
                .usuarioCambio(usuario)
                .comentario(comentario)
                .build();
        historialRepositorio.save(historial);
    }
}