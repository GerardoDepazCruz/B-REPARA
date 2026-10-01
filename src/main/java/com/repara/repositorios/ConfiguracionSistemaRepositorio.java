package com.repara.repositorios;

import com.repara.entidades.ConfiguracionSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionSistemaRepositorio extends JpaRepository<ConfiguracionSistema, Integer> {
    
    Optional<ConfiguracionSistema> findByClave(String clave);
    
    boolean existsByClave(String clave);
}