package com.atencion.atencion.dto;

import lombok.Data;

@Data
public class IncidenciaRequestDTO {
    private Long usuarioId;
    private String descripcion;
    private int priority;
}