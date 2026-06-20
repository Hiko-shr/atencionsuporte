package com.atencion.atencion.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private Long sucursal_id; 
}