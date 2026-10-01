package com.repara.servicios;

import com.repara.dtos.peticion.PagoPeticion;
import com.repara.entidades.Pago;
import com.repara.entidades.PagoMetodo;
import com.repara.entidades.SolicitudServicio;
import com.repara.excepciones.ReglaNegocioException;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.PagoMetodoRepositorio;
import com.repara.repositorios.PagoRepositorio;
import com.repara.repositorios.SolicitudServicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PagoServicio {

    @Autowired
    private PagoRepositorio pagoRepositorio;

    @Autowired
    private PagoMetodoRepositorio metodoRepositorio;

    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Transactional
    public Pago registrarPagoInicial(Long idSolicitud, PagoPeticion peticion) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        if (solicitud.getEstado() != SolicitudServicio.Estado.ACEPTADA) {
            throw new ReglaNegocioException("La solicitud debe estar aceptada por un técnico");
        }

        PagoMetodo metodo = metodoRepositorio.findById(peticion.getIdMetodoPago())
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado"));

        BigDecimal montoInicial = new BigDecimal("20.00");
        BigDecimal comision = montoInicial.multiply(metodo.getComisionPorcentaje())
                .divide(new BigDecimal("100"));

        Pago pago = Pago.builder()
                .solicitud(solicitud)
                .metodoPago(metodo)
                .tipoPago(Pago.TipoPago.INICIAL)
                .descripcion("Pago inicial por traslado y diagnóstico")
                .monto(montoInicial)
                .porcentajeComision(metodo.getComisionPorcentaje())
                .montoComision(comision)
                .montoNetoTecnico(montoInicial.subtract(comision))
                .estado(Pago.Estado.PAGADO)
                .referenciaTransaccion(UUID.randomUUID().toString())
                .numeroOperacion(peticion.getNumeroOperacion())
                .fechaPago(LocalDateTime.now())
                .build();

        pagoRepositorio.save(pago);

        // Actualizar solicitud
        solicitud.setMontoInicial(montoInicial);
        solicitud.setEstado(SolicitudServicio.Estado.EN_CAMINO);
        solicitudRepositorio.save(solicitud);

        return pago;
    }

    @Transactional
    public Pago registrarPagoFinal(Long idSolicitud, PagoPeticion peticion) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        if (solicitud.getCostoFinal() == null) {
            throw new ReglaNegocioException("La solicitud no tiene costo final definido");
        }

        PagoMetodo metodo = metodoRepositorio.findById(peticion.getIdMetodoPago())
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado"));

        BigDecimal montoFinal = solicitud.getCostoFinal().subtract(
                solicitud.getMontoInicial() != null ? solicitud.getMontoInicial() : BigDecimal.ZERO);
        
        BigDecimal comision = montoFinal.multiply(metodo.getComisionPorcentaje())
                .divide(new BigDecimal("100"));

        Pago pago = Pago.builder()
                .solicitud(solicitud)
                .metodoPago(metodo)
                .tipoPago(Pago.TipoPago.COMPLEMENTARIO)
                .descripcion("Pago final del servicio")
                .monto(montoFinal)
                .porcentajeComision(metodo.getComisionPorcentaje())
                .montoComision(comision)
                .montoNetoTecnico(montoFinal.subtract(comision))
                .estado(Pago.Estado.PAGADO)
                .referenciaTransaccion(UUID.randomUUID().toString())
                .numeroOperacion(peticion.getNumeroOperacion())
                .fechaPago(LocalDateTime.now())
                .build();

        pagoRepositorio.save(pago);

        // Actualizar solicitud
        solicitud.setMontoFinal(solicitud.getCostoFinal());
        solicitud.setEstado(SolicitudServicio.Estado.COMPLETADO);
        solicitud.setFechaFinalizacion(LocalDateTime.now());
        solicitudRepositorio.save(solicitud);

        return pago;
    }

    public List<Pago> listarPorSolicitud(Long idSolicitud) {
        return pagoRepositorio.findBySolicitudIdSolicitud(idSolicitud);
    }

    public List<PagoMetodo> listarMetodosActivos() {
        return metodoRepositorio.findByEstado(PagoMetodo.Estado.ACTIVO);
    }
}