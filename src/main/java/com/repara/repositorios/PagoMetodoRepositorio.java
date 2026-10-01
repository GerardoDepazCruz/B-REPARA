package com.repara.repositorios;

import com.repara.entidades.PagoMetodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoMetodoRepositorio extends JpaRepository<PagoMetodo, Integer> {
    
    List<PagoMetodo> findByEstado(PagoMetodo.Estado estado);
    
    Optional<PagoMetodo> findByNombre(String nombre);
}