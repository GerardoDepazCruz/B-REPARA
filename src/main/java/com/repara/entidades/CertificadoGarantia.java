package com.repara.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "certificados_garantia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificadoGarantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Long idCertificado;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudServicio solicitud;

    @Column(name = "codigo_certificado", nullable = false, unique = true, length = 50)
    private String codigoCertificado;

    @Column(name = "url_pdf", nullable = false)
    private String urlPdf;

    @Column(name = "fecha_emision", nullable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;

    @Builder.Default
    @Column(name = "enviado_correo", nullable = false)
    private Boolean enviadoCorreo = false;

    @PrePersist
    protected void alCrear() {
        fechaEmision = LocalDateTime.now();
    }
}