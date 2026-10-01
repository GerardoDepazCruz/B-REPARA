package com.repara.repositorios;

import com.repara.entidades.CertificadoGarantia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificadoGarantiaRepositorio extends JpaRepository<CertificadoGarantia, Long> {
    
    Optional<CertificadoGarantia> findBySolicitudIdSolicitud(Long idSolicitud);
    
    Optional<CertificadoGarantia> findByCodigoCertificado(String codigo);
}