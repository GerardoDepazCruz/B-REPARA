package com.repara.repositorios;

import com.repara.entidades.TecnicoEspecialidad;
import com.repara.entidades.TecnicoEspecialidadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TecnicoEspecialidadRepositorio extends JpaRepository<TecnicoEspecialidad, TecnicoEspecialidadId> {
    
    List<TecnicoEspecialidad> findByTecnicoIdTecnico(Long idTecnico);
    
    List<TecnicoEspecialidad> findByCategoriaIdCategoria(Integer idCategoria);
}