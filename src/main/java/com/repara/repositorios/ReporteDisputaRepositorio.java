package com.repara.repositorios;

import com.repara.entidades.ReporteDisputa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteDisputaRepositorio extends JpaRepository<ReporteDisputa, Long> {
    
    List<ReporteDisputa> findByEstado(ReporteDisputa.Estado estado);
    
    List<ReporteDisputa> findByReportanteIdUsuario(Long idUsuario);
}