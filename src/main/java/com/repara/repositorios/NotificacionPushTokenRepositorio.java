package com.repara.repositorios;

import com.repara.entidades.NotificacionPushToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionPushTokenRepositorio extends JpaRepository<NotificacionPushToken, Long> {
    
    List<NotificacionPushToken> findByUsuarioIdUsuarioAndActivoTrue(Long idUsuario);
    
    Optional<NotificacionPushToken> findByTokenFcm(String tokenFcm);
}