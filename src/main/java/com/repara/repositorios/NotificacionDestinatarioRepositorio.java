package com.repara.repositorios;

import com.repara.entidades.NotificacionDestinatario;
import com.repara.entidades.NotificacionDestinatarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionDestinatarioRepositorio extends JpaRepository<NotificacionDestinatario, NotificacionDestinatarioId> {
    
    List<NotificacionDestinatario> findByUsuarioIdUsuario(Long idUsuario);
    
    List<NotificacionDestinatario> findByUsuarioIdUsuarioAndLeidaFalse(Long idUsuario);
}