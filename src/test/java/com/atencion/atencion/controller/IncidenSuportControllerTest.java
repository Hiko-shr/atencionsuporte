package com.atencion.atencion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.atencion.atencion.dto.IncidenciaRequestDTO;
import com.atencion.atencion.dto.IncidenciaResponseDTO;
import com.atencion.atencion.service.IncidenSuportService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = IncidenSuportController.class)
@AutoConfigureMockMvc(addFilters = false)
class IncidenSuportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidenSuportService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearIncidencia() throws Exception {
        IncidenciaRequestDTO request = new IncidenciaRequestDTO();
        request.setUsuarioId(1L);
        request.setDescripcion("Error general");
        request.setPriority(1);

        IncidenciaResponseDTO response = new IncidenciaResponseDTO();
        response.setId(1L);
        response.setDescripcion("Error general");
        response.setEstado("ABIERTO");
        response.setPrioridad(1);

        when(service.crearIncidencia(any(IncidenciaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/incidencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Error general"))
                .andExpect(jsonPath("$.estado").value("ABIERTO"));
    }

    @Test
    void testGetIncidencias() throws Exception {
        IncidenciaResponseDTO response = new IncidenciaResponseDTO();
        response.setId(1L);
        response.setDescripcion("Error general");
        response.setEstado("ABIERTO");

        when(service.obtenerTodasConUsuarios()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/incidencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Error general"));
    }
}