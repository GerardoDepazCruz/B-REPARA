package com.repara.repositorios;

import com.repara.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsByCorreo(String correo);
    
    boolean existsByDni(String dni);
    
    List<Usuario> findByRol(Usuario.Rol rol);
    
    List<Usuario> findByEstado(Usuario.Estado estado);
    
    List<Usuario> findByRolAndEstado(Usuario.Rol rol, Usuario.Estado estado);
    
    List<Usuario> findByEsTecnicoTrue();
    
    Optional<Usuario> findByTokenVerificacion(String token);
}