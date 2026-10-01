package com.repara.repositorios;

import com.repara.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Integer> {
    
    Optional<Categoria> findByNombre(String nombre);
    
    List<Categoria> findByEstado(Categoria.Estado estado);
    
    boolean existsByNombre(String nombre);
}