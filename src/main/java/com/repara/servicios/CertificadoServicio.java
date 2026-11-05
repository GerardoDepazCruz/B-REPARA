package com.repara.servicios;

import com.repara.entidades.CertificadoGarantia;
import com.repara.entidades.SolicitudServicio;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.CertificadoGarantiaRepositorio;
import com.repara.repositorios.SolicitudServicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CertificadoServicio {

    @Autowired
    private CertificadoGarantiaRepositorio certificadoRepositorio;
    @Autowired
    private SolicitudServicioRepositorio solicitudRepositorio;

    @Transactional
    public CertificadoGarantia generarCertificado(Long idSolicitud) {
        // Verificar si ya existe
        return certificadoRepositorio.findBySolicitudIdSolicitud(idSolicitud).orElseGet(() -> {
            SolicitudServicio solicitud = solicitudRepositorio.findById(idSolicitud)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

            String codigo = "GAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            CertificadoGarantia certificado = CertificadoGarantia.builder()
                    .solicitud(solicitud)
                    .codigoCertificado(codigo)
                    .urlPdf("https://repara.com/garantias/" + codigo + ".pdf") // URL simulada
                    .fechaEmision(LocalDateTime.now())
                    .fechaExpiracion(LocalDate.now().plusMonths(3)) // 3 meses de garantía
                    .enviadoCorreo(true)
                    .build();

            return certificadoRepositorio.save(certificado);
        });
    }
}