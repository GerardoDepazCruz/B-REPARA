package com.repara.repositorios;

import com.repara.entidades.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenciaRepositorio extends JpaRepository<Evidencia, Long> {
    
    List<Evidencia> findBySolicitudIdSolicitud(Long idSolicitud);
    
    List<Evidencia> findBySolicitudIdSolicitudAndEtapa(Long idSolicitud, Evidencia.Etapa etapa);
}