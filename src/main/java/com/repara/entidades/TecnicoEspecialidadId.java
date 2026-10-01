package com.repara.entidades;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoEspecialidadId implements Serializable {
    private Long tecnico;
    private Integer categoria;
}