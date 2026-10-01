package com.repara.repositorios;

import com.repara.entidades.InsigniaTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsigniaTecnicoRepositorio extends JpaRepository<InsigniaTecnico, Long> {
    
    List<InsigniaTecnico> findByTecnicoIdTecnico(Long idTecnico);
}