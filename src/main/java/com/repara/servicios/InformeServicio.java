package com.repara.servicios;

import com.repara.dtos.peticion.InformePeticion;
import com.repara.entidades.*;
import com.repara.excepciones.ReglaNegocioException;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InformeServicio {

    @Autowired
    private InformeTecnicoRepositorio informeRepositorio;

    @Autowired
    private RepuestoUtilizadoRepositorio repuestoRepositorio;

    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Transactional
    public InformeTecnico crearInforme(Long idTecnico, InformePeticion peticion) {
        SolicitudServicio solicitud = solicitudRepositorio.findById(peticion.getIdSolicitud())
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        Tecnico tecnico = tecnicoRepositorio.findById(idTecnico)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado"));

        if (informeRepositorio.findBySolicitudIdSolicitud(peticion.getIdSolicitud()).isPresent()) {
            throw new ReglaNegocioException("Ya existe un informe para esta solicitud");
        }

        InformeTecnico informe = InformeTecnico.builder()
                .solicitud(solicitud)
                .tecnico(tecnico)
                .resumenTrabajo(peticion.getResumenTrabajo())
                .diagnosticoDetallado(peticion.getDiagnosticoDetallado())
                .accionesRealizadas(peticion.getAccionesRealizadas())
                .tiempoEstimadoMin(peticion.getTiempoEstimadoMin())
                .tiempoRealMin(peticion.getTiempoRealMin())
                .manoObraCosto(peticion.getManoObraCosto())
                .observaciones(peticion.getObservaciones())
                .recomendacionesCliente(peticion.getRecomendacionesCliente())
                .estadoFinal(InformeTecnico.EstadoFinal.valueOf(peticion.getEstadoFinal()))
                .visibleCliente(true)
                .build();

        informeRepositorio.save(informe);

        // Guardar repuestos
        if (peticion.getRepuestos() != null) {
            for (InformePeticion.RepuestoPeticion r : peticion.getRepuestos()) {
                RepuestoUtilizado repuesto = RepuestoUtilizado.builder()
                        .informe(informe)
                        .nombreRepuesto(r.getNombreRepuesto())
                        .descripcion(r.getDescripcion())
                        .marca(r.getMarca())
                        .modelo(r.getModelo())
                        .cantidad(r.getCantidad())
                        .costoUnitario(r.getCostoUnitario())
                        .costoTotal(r.getCostoUnitario().multiply(new java.math.BigDecimal(r.getCantidad())))
                        .proveedor(r.getProveedor())
                        .garantiaMeses(r.getGarantiaMeses())
                        .build();
                repuestoRepositorio.save(repuesto);
            }
        }

        return informe;
    }

    public InformeTecnico obtenerPorSolicitud(Long idSolicitud) {
        return informeRepositorio.findBySolicitudIdSolicitud(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Informe no encontrado"));
    }

    public List<InformeTecnico> listarPorTecnico(Long idTecnico) {
        return informeRepositorio.findByTecnicoIdTecnico(idTecnico);
    }

    public List<RepuestoUtilizado> listarRepuestos(Long idInforme) {
        return repuestoRepositorio.findByInformeIdInforme(idInforme);
    }
}