package com.repara.repositorios;

import com.repara.entidades.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByAdminEmisorIdUsuario(Long idAdmin);
    
    List<Notificacion> findByEnviadaFalse();
}