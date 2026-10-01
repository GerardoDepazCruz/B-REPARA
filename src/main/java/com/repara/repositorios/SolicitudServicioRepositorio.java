package com.repara.repositorios;

import com.repara.entidades.SolicitudServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudServicioRepositorio extends JpaRepository<SolicitudServicio, Long> {
    
    List<SolicitudServicio> findByClienteIdUsuarioOrderByFechaSolicitudDesc(Long idCliente);
    
    List<SolicitudServicio> findByTecnicoIdTecnicoOrderByFechaSolicitudDesc(Long idTecnico);
    
    List<SolicitudServicio> findByEstado(SolicitudServicio.Estado estado);
    
    List<SolicitudServicio> findByEstadoOrderByFechaSolicitudAsc(SolicitudServicio.Estado estado);
    
    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado = 'PENDIENTE_ASIGNACION' " +
           "ORDER BY s.fechaSolicitud ASC")
    List<SolicitudServicio> findPendientesAsignacion();
    
    long countByEstado(SolicitudServicio.Estado estado);
}