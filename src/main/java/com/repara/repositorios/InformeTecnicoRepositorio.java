package com.repara.repositorios;

import com.repara.entidades.InformeTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InformeTecnicoRepositorio extends JpaRepository<InformeTecnico, Long> {
    
    Optional<InformeTecnico> findBySolicitudIdSolicitud(Long idSolicitud);
    
    List<InformeTecnico> findByTecnicoIdTecnico(Long idTecnico);
}