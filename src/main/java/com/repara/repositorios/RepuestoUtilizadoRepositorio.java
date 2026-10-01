package com.repara.repositorios;

import com.repara.entidades.RepuestoUtilizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepuestoUtilizadoRepositorio extends JpaRepository<RepuestoUtilizado, Long> {
    
    List<RepuestoUtilizado> findByInformeIdInforme(Long idInforme);
}