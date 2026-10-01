package com.repara.repositorios;

import com.repara.entidades.MensajeIa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeIaRepositorio extends JpaRepository<MensajeIa, Long> {
    
    List<MensajeIa> findBySolicitudIdSolicitudOrderByOrdenMensajeAsc(Long idSolicitud);
}