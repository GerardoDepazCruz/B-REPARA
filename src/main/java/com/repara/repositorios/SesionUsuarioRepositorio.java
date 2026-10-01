package com.repara.repositorios;

import com.repara.entidades.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionUsuarioRepositorio extends JpaRepository<SesionUsuario, Long> {
    
    Optional<SesionUsuario> findByTokenJwt(String tokenJwt);
    
    List<SesionUsuario> findByUsuarioIdUsuarioAndActivaTrue(Long idUsuario);
}