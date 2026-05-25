package com.atencion.atencion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.atencion.atencion.dto.IncidenciaRequestDTO;
import com.atencion.atencion.service.IncidenSuportService;

@RestController
@RequestMapping("api/v1/incidencias")
public class IncidenSuportController {

    @Autowired
    private IncidenSuportService incidenciaService;

    @PostMapping
    public ResponseEntity<?> crearIncidencia(@RequestBody IncidenciaRequestDTO dto) {
        return ResponseEntity.ok(
                incidenciaService
                .crearIncidencia(dto));
    }

    @GetMapping
    public ResponseEntity<?> getIncidencias() {
        return ResponseEntity.ok(
                incidenciaService
                .obtenerTodasConUsuarios());
    }
}