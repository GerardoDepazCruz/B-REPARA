package com.repara.repositorios;

import com.repara.entidades.EspecialidadSolicitud;
import com.repara.entidades.EspecialidadSolicitudId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadSolicitudRepositorio extends JpaRepository<EspecialidadSolicitud, EspecialidadSolicitudId> {
    
    List<EspecialidadSolicitud> findBySolicitudIdSolicitud(Long idSolicitud);
}