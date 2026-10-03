package com.repara.servicios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${backup.directory.path:./backups}")
    private String backupDir;

    @Value("${backup.max.files:30}")
    private int maxFiles;

    @Value("${backup.mysqldump.path:mysqldump}")
    private String rutaMysqldump;

    /**
     * Backup automático diario a las 02:00 AM
     * Cron: segundo minuto hora día-del-mes mes día-de-semana
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void realizarBackupDiario() {
        logger.info("🔄 Iniciando backup automático diario de la base de datos...");
        ejecutarBackup();
    }

    /**
     * Método principal que ejecuta el backup
     */
    public String ejecutarBackup() {
        try {
            // 1. Crear carpeta de backups si no existe
            File directorio = new File(backupDir);
            if (!directorio.exists()) {
                boolean creado = directorio.mkdirs();
                if (creado) {
                    logger.info("📁 Carpeta de backups creada: {}", directorio.getAbsolutePath());
                }
            }

            // 2. Extraer el nombre de la BD de la URL
            String dbName = extraerNombreBD(dbUrl);

            // 3. Nombre del archivo con timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String nombreArchivo = "repara_db_" + timestamp + ".sql";
            String rutaCompleta = new File(backupDir, nombreArchivo).getAbsolutePath();

            // 4. Comando mysqldump usando la ruta configurable
            String[] comando = {
                    rutaMysqldump,
                    "-u" + dbUser,
                    "-p" + dbPassword,
                    dbName,
                    "-r", rutaCompleta
            };

            logger.info("⏳ Ejecutando: {} -u{} -p**** {}", rutaMysqldump, dbUser, dbName);

            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.redirectErrorStream(true);
            Process proceso = pb.start();
            int resultado = proceso.waitFor();

            if (resultado == 0) {
                logger.info("✅ Backup exitoso: {}", rutaCompleta);
                limpiarBackupsAntiguos();
                return "Backup exitoso: " + rutaCompleta;
            } else {
                logger.error("❌ Error al ejecutar mysqldump. Código de salida: {}", resultado);
                return "Error al crear backup. Código: " + resultado;
            }

        } catch (IOException | InterruptedException e) {
            logger.error("❌ Error durante el backup: {}", e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Extrae el nombre de la BD desde la URL de conexión
     * Ej: jdbc:mysql://localhost:3306/repara_db?... → repara_db
     */
    private String extraerNombreBD(String url) {
        try {
            String sinProtocolo = url.replace("jdbc:mysql://", "");
            String despuesSlash = sinProtocolo.substring(sinProtocolo.indexOf("/") + 1);
            return despuesSlash.split("\\?")[0];
        } catch (Exception e) {
            logger.warn("No se pudo extraer el nombre de la BD, usando por defecto: repara_db");
            return "repara_db";
        }
    }

    /**
     * Elimina backups antiguos manteniendo solo los últimos N
     */
    private void limpiarBackupsAntiguos() {
        File directorio = new File(backupDir);
        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".sql"));

        if (archivos == null || archivos.length <= maxFiles) {
            return;
        }

        // Ordenar por fecha de modificación (más antiguos primero)
        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        int eliminar = archivos.length - maxFiles;
        for (int i = 0; i < eliminar; i++) {
            if (archivos[i].delete()) {
                logger.info("🗑️ Backup antiguo eliminado: {}", archivos[i].getName());
            }
        }
    }
}