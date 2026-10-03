package com.repara.controladores;

import com.repara.servicios.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    /**
     * Endpoint para forzar un backup manual (solo ADMIN)
     * POST /api/admin/backup/ejecutar
     */
    @PostMapping("/ejecutar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> ejecutarBackupManual() {
        String resultado = backupService.ejecutarBackup();
        return ResponseEntity.ok(Map.of(
                "mensaje", "Backup ejecutado",
                "resultado", resultado
        ));
    }
}