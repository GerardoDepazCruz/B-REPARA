package com.repara.repositorios;

import com.repara.entidades.PinVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PinVerificacionRepositorio extends JpaRepository<PinVerificacion, Long> {
    
    Optional<PinVerificacion> findBySolicitudIdSolicitud(Long idSolicitud);
    
    Optional<PinVerificacion> findBySolicitudIdSolicitudAndEstado(Long idSolicitud, PinVerificacion.Estado estado);
}