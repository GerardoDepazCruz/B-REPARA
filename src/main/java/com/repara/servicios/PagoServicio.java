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

        // 🔥 Usamos el monto enviado desde el frontend o el costo_final de la solicitud
        BigDecimal montoFinal = peticion.getMonto() != null 
                ? peticion.getMonto() 
                : solicitud.getCostoFinal();

        if (montoFinal == null) {
            throw new ReglaNegocioException("El monto final es requerido");
        }

        PagoMetodo metodo = metodoRepositorio.findById(peticion.getIdMetodoPago())
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado"));

        BigDecimal montoACobrar = montoFinal.subtract(
                solicitud.getMontoInicial() != null ? solicitud.getMontoInicial() : BigDecimal.ZERO);
        
        BigDecimal comision = montoACobrar.multiply(metodo.getComisionPorcentaje())
                .divide(new BigDecimal("100"));

        Pago pago = Pago.builder()
                .solicitud(solicitud)
                .metodoPago(metodo)
                .tipoPago(Pago.TipoPago.COMPLEMENTARIO)
                .descripcion("Pago final del servicio")
                .monto(montoACobrar)
                .porcentajeComision(metodo.getComisionPorcentaje())
                .montoComision(comision)
                .montoNetoTecnico(montoACobrar.subtract(comision))
                .estado(Pago.Estado.PAGADO)
                .referenciaTransaccion(UUID.randomUUID().toString())
                .numeroOperacion(peticion.getNumeroOperacion())
                .fechaPago(LocalDateTime.now())
                .build();

        pagoRepositorio.save(pago);

        // 🔥 Actualizar solicitud a REPARACION_EN_PROCESO
        solicitud.setCostoFinal(montoFinal);
        solicitud.setMontoFinal(montoFinal);
        solicitud.setEstado(SolicitudServicio.Estado.REPARACION_EN_PROCESO);
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