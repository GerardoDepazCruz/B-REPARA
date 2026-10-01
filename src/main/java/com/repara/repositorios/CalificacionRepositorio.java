package com.repara.repositorios;

import com.repara.entidades.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long> {
    
    List<Calificacion> findByCalificadoIdUsuario(Long idUsuario);
    
    List<Calificacion> findBySolicitudIdSolicitud(Long idSolicitud);
    
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.calificado.idUsuario = :idUsuario")
    Double calcularPromedioCalificaciones(@Param("idUsuario") Long idUsuario);
}