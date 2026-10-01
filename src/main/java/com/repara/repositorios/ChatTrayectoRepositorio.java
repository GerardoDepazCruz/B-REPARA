package com.repara.repositorios;

import com.repara.entidades.ChatTrayecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatTrayectoRepositorio extends JpaRepository<ChatTrayecto, Long> {
    
    List<ChatTrayecto> findBySolicitudIdSolicitudOrderByFechaEnvioAsc(Long idSolicitud);
    
    List<ChatTrayecto> findBySolicitudIdSolicitudAndLeidoFalse(Long idSolicitud);
}