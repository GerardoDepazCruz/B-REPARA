package com.repara.repositorios;

import com.repara.entidades.PostulacionTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostulacionTecnicoRepositorio extends JpaRepository<PostulacionTecnico, Long> {
    
    List<PostulacionTecnico> findByEstado(PostulacionTecnico.Estado estado);
    
    List<PostulacionTecnico> findByUsuarioIdUsuario(Long idUsuario);
    
    Optional<PostulacionTecnico> findFirstByUsuarioIdUsuarioOrderByFechaPostulacionDesc(Long idUsuario);
    
    List<PostulacionTecnico> findByUsuarioIdUsuarioAndEstado(Long idUsuario, PostulacionTecnico.Estado estado);
}