package com.repara.repositorios;

import com.repara.entidades.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {
    
    List<Pago> findBySolicitudIdSolicitud(Long idSolicitud);
    
    List<Pago> findBySolicitudIdSolicitudAndTipoPago(Long idSolicitud, Pago.TipoPago tipoPago);
    
    List<Pago> findByEstado(Pago.Estado estado);
    
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.estado = 'PAGADO'")
    BigDecimal sumTotalPagado();
    
    @Query("SELECT COALESCE(SUM(p.montoComision), 0) FROM Pago p WHERE p.estado = 'PAGADO'")
    BigDecimal sumTotalComisiones();
}