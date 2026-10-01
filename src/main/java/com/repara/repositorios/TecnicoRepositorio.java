package com.repara.repositorios;

import com.repara.entidades.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepositorio extends JpaRepository<Tecnico, Long> {
    
    Optional<Tecnico> findByUsuarioIdUsuario(Long idUsuario);
    
    List<Tecnico> findByDisponibleTrueAndEstado(Tecnico.Estado estado);
    
    List<Tecnico> findByEstado(Tecnico.Estado estado);
    
    @Query("SELECT t FROM Tecnico t JOIN TecnicoEspecialidad te ON te.tecnico.idTecnico = t.idTecnico " +
           "WHERE te.categoria.idCategoria = :idCategoria " +
           "AND t.disponible = true AND t.estado = 'ACTIVO'")
    List<Tecnico> findDisponiblesPorCategoria(@Param("idCategoria") Integer idCategoria);
    
    @Query("SELECT t FROM Tecnico t ORDER BY t.calificacionPromedio DESC")
    List<Tecnico> findTopCalificados();
}