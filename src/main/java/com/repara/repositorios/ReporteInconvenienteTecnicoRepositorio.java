package com.repara.repositorios;

import com.repara.entidades.ReporteInconvenienteTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteInconvenienteTecnicoRepositorio extends JpaRepository<ReporteInconvenienteTecnico, Long> {
    
    List<ReporteInconvenienteTecnico> findByEstado(ReporteInconvenienteTecnico.Estado estado);
    
    List<ReporteInconvenienteTecnico> findByTecnicoIdTecnico(Long idTecnico);
}