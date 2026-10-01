package com.repara.repositorios;

import com.repara.entidades.HistorialEstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoSolicitudRepositorio extends JpaRepository<HistorialEstadoSolicitud, Long> {
    
    List<HistorialEstadoSolicitud> findBySolicitudIdSolicitudOrderByFechaCambioAsc(Long idSolicitud);
}