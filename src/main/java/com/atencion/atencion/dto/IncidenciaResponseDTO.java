package com.atencion.atencion.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidenciaResponseDTO {
    private Long id;
    private String descripcion;
    private String estado;
    private int prioridad;
    private LocalDateTime fechaReporte;
    private UsuarioDTO usuario; 
}