package com.repara.repositorios;

import com.repara.entidades.LogActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogActividadRepositorio extends JpaRepository<LogActividad, Long> {
    
    List<LogActividad> findByUsuarioIdUsuarioOrderByFechaAccionDesc(Long idUsuario);
    
    List<LogActividad> findByAccion(String accion);
}