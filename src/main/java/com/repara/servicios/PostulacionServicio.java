package com.repara.servicios;

import com.repara.dtos.peticion.PostulacionPeticion;
import com.repara.entidades.PostulacionTecnico;
import com.repara.entidades.Tecnico;
import com.repara.entidades.Usuario;
import com.repara.excepciones.ReglaNegocioException;
import com.repara.excepciones.RecursoNoEncontradoException;
import com.repara.repositorios.PostulacionTecnicoRepositorio;
import com.repara.repositorios.TecnicoRepositorio;
import com.repara.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostulacionServicio {

    @Autowired
    private PostulacionTecnicoRepositorio postulacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Transactional
    public PostulacionTecnico crearPostulacion(Long idUsuario, PostulacionPeticion peticion) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // Verificar si ya tiene postulación pendiente
        List<PostulacionTecnico> pendientes = postulacionRepositorio
                .findByUsuarioIdUsuarioAndEstado(idUsuario, PostulacionTecnico.Estado.PENDIENTE);
        
        if (!pendientes.isEmpty()) {
            throw new ReglaNegocioException("Ya tienes una postulación pendiente");
        }

        if (Boolean.TRUE.equals(usuario.getEsTecnico())) {
            throw new ReglaNegocioException("Ya eres técnico verificado");
        }

        PostulacionTecnico postulacion = PostulacionTecnico.builder()
                .usuario(usuario)
                .especialidadDeclarada(peticion.getEspecialidadDeclarada())
                .aniosExperiencia(peticion.getAniosExperiencia())
                .cvUrl(peticion.getCvUrl())
                .certificadosUrl(peticion.getCertificadosUrl())
                .dniDocumentoUrl(peticion.getDniDocumentoUrl())
                .estado(PostulacionTecnico.Estado.PENDIENTE)
                .build();

        return postulacionRepositorio.save(postulacion);
    }

    public List<PostulacionTecnico> listarPendientes() {
        return postulacionRepositorio.findByEstado(PostulacionTecnico.Estado.PENDIENTE);
    }

    public List<PostulacionTecnico> listarTodas() {
        return postulacionRepositorio.findAll();
    }

    @Transactional
    public PostulacionTecnico aprobar(Long idPostulacion, Long idAdmin) {
        PostulacionTecnico postulacion = postulacionRepositorio.findById(idPostulacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Postulación no encontrada"));

        if (postulacion.getEstado() != PostulacionTecnico.Estado.PENDIENTE) {
            throw new ReglaNegocioException("La postulación ya fue revisada");
        }

        Usuario admin = usuarioRepositorio.findById(idAdmin)
                .orElseThrow(() -> new RecursoNoEncontradoException("Admin no encontrado"));

        postulacion.setEstado(PostulacionTecnico.Estado.ACEPTADO);
        postulacion.setFechaRevision(LocalDateTime.now());
        postulacion.setAdminRevisor(admin);
        postulacionRepositorio.save(postulacion);

        // Actualizar usuario
        Usuario usuario = postulacion.getUsuario();
        usuario.setEsTecnico(true);
        usuarioRepositorio.save(usuario);

        // Crear registro de técnico
        Tecnico tecnico = Tecnico.builder()
                .usuario(usuario)
                .postulacionOrigen(postulacion)
                .disponible(true)
                .estado(Tecnico.Estado.ACTIVO)
                .build();
        tecnicoRepositorio.save(tecnico);

        return postulacion;
    }

    @Transactional
    public PostulacionTecnico rechazar(Long idPostulacion, Long idAdmin, String motivo) {
        PostulacionTecnico postulacion = postulacionRepositorio.findById(idPostulacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Postulación no encontrada"));

        if (postulacion.getEstado() != PostulacionTecnico.Estado.PENDIENTE) {
            throw new ReglaNegocioException("La postulación ya fue revisada");
        }

        Usuario admin = usuarioRepositorio.findById(idAdmin)
                .orElseThrow(() -> new RecursoNoEncontradoException("Admin no encontrado"));

        postulacion.setEstado(PostulacionTecnico.Estado.RECHAZADO);
        postulacion.setMotivoRechazo(motivo);
        postulacion.setFechaRevision(LocalDateTime.now());
        postulacion.setAdminRevisor(admin);

        return postulacionRepositorio.save(postulacion);
    }

    public List<PostulacionTecnico> listarPorUsuario(Long idUsuario) {
        return postulacionRepositorio.findByUsuarioIdUsuario(idUsuario);
    }
}