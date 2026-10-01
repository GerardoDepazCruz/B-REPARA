package com.repara.servicios;

import com.repara.entidades.PinVerificacion;
import com.repara.entidades.SolicitudServicio;
import com.repara.entidades.Tecnico;
import com.repara.excepciones.ReglaNegocioException;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.PinVerificacionRepositorio;
import com.repara.repositorios.SolicitudServicioRepositorio;
import com.repara.repositorios.TecnicoRepositorio;
import com.repara.utilidades.PinGenerador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PinServicio {

    @Autowired
    private PinVerificacionRepositorio pinRepositorio;

    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Transactional
    public PinVerificacion generarPin(Long idSolicitud) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        return pinRepositorio.findBySolicitudIdSolicitudAndEstado(idSolicitud, PinVerificacion.Estado.ACTIVO)
                .orElseGet(() -> {
                    PinVerificacion pin = PinVerificacion.builder()
                            .solicitud(solicitud)
                            .codigoPin(PinGenerador.generarPin4Digitos())
                            .estado(PinVerificacion.Estado.ACTIVO)
                            .fechaExpiracion(LocalDateTime.now().plusMinutes(30))
                            .build();
                    return pinRepositorio.save(pin);
                });
    }

    @Transactional
    public boolean verificarPin(Long idSolicitud, String pin, Long idTecnico) {
        PinVerificacion pinVerif = pinRepositorio
                .findBySolicitudIdSolicitudAndEstado(idSolicitud, PinVerificacion.Estado.ACTIVO)
                .orElseThrow(() -> new ReglaNegocioException("No hay PIN activo para esta solicitud"));

        if (pinVerif.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            pinVerif.setEstado(PinVerificacion.Estado.EXPIRADO);
            pinRepositorio.save(pinVerif);
            throw new ReglaNegocioException("El PIN ha expirado");
        }

        if (!pinVerif.getCodigoPin().equals(pin)) {
            pinVerif.setIntentosFallidos(pinVerif.getIntentosFallidos() + 1);
            if (pinVerif.getIntentosFallidos() >= pinVerif.getMaxIntentos()) {
                pinVerif.setEstado(PinVerificacion.Estado.BLOQUEADO);
            }
            pinRepositorio.save(pinVerif);
            throw new ReglaNegocioException("PIN incorrecto");
        }

        pinVerif.setEstado(PinVerificacion.Estado.USADO);
        pinVerif.setFechaUso(LocalDateTime.now());

        Tecnico tecnico = tecnicoRepositorio.findById(idTecnico)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado"));
        pinVerif.setTecnicoVerificador(tecnico);
        pinRepositorio.save(pinVerif);

        SolicitudServicio solicitud = pinVerif.getSolicitud();
        solicitud.setPinVerificado(true);
        solicitud.setFechaPinVerificado(LocalDateTime.now());
        solicitud.setEstado(SolicitudServicio.Estado.EN_DIAGNOSTICO);
        solicitudRepositorio.save(solicitud);

        return true;
    }

    public PinVerificacion obtenerPinActivo(Long idSolicitud) {
        return pinRepositorio.findBySolicitudIdSolicitudAndEstado(idSolicitud, PinVerificacion.Estado.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay PIN activo para esta solicitud"));
    }
}