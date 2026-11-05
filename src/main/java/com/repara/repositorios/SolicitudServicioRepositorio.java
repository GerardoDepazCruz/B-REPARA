package com.repara.repositorios;

import com.repara.entidades.SolicitudServicio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudServicioRepositorio extends JpaRepository<SolicitudServicio, Long> {
    
    // 🔥 @EntityGraph fuerza a que traiga al técnico, su usuario, el cliente y la categoría
    @EntityGraph(attributePaths = {"tecnico", "tecnico.usuario", "cliente", "categoria"})
    List<SolicitudServicio> findByClienteIdUsuarioOrderByFechaSolicitudDesc(Long idCliente);
    
    @EntityGraph(attributePaths = {"tecnico", "tecnico.usuario", "cliente", "categoria"})
    List<SolicitudServicio> findByTecnicoIdTecnicoOrderByFechaSolicitudDesc(Long idTecnico);
    
    @EntityGraph(attributePaths = {"tecnico", "tecnico.usuario", "cliente", "categoria"})
    List<SolicitudServicio> findByEstado(SolicitudServicio.Estado estado);
    
    @EntityGraph(attributePaths = {"tecnico", "tecnico.usuario", "cliente", "categoria"})
    List<SolicitudServicio> findByEstadoOrderByFechaSolicitudAsc(SolicitudServicio.Estado estado);
    
    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado = 'PENDIENTE_ASIGNACION' " +
           "ORDER BY s.fechaSolicitud ASC")
    List<SolicitudServicio> findPendientesAsignacion();
    
    long countByEstado(SolicitudServicio.Estado estado);
}