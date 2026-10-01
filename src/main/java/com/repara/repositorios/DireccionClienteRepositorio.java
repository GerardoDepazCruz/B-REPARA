package com.repara.repositorios;

import com.repara.entidades.DireccionCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionClienteRepositorio extends JpaRepository<DireccionCliente, Long> {
    
    List<DireccionCliente> findByUsuarioIdUsuario(Long idUsuario);
    
    List<DireccionCliente> findByUsuarioIdUsuarioAndEstado(Long idUsuario, DireccionCliente.Estado estado);
    
    Optional<DireccionCliente> findByUsuarioIdUsuarioAndEsPrincipalTrue(Long idUsuario);
}