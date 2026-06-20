package com.atencion.atencion.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.atencion.atencion.dto.*;
import java.time.LocalDateTime;

@Service
public class IncidenSuportService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    private final String CLIENTES_URL = "http://localhost:8081/api/v1/usuarios";

    private final List<IncidenciaResponseDTO> incidenciasMemoria = new ArrayList<>();
    private long idContador = 1;

    public IncidenciaResponseDTO crearIncidencia(IncidenciaRequestDTO dto) {
        
        if (dto.getUsuarioId() == null) {
            throw new RuntimeException("El id del usuario es obligatorio para registrar la incidencia.");
        }
        
        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción de la incidencia no puede estar vacía.");
        }
        
        if (dto.getPriority() <= 0) {
            throw new RuntimeException("La prioridad de la incidencia debe ser un número mayor a 0.");
        }

        IncidenciaResponseDTO nuevaIncidencia = new IncidenciaResponseDTO();
        nuevaIncidencia.setId(idContador++);
        nuevaIncidencia.setDescripcion(dto.getDescripcion());
        nuevaIncidencia.setPrioridad(dto.getPriority());
        nuevaIncidencia.setEstado("ABIERTO");
        nuevaIncidencia.setFechaReporte(LocalDateTime.now());
        
        try {
            String urlEspecifica = CLIENTES_URL + "/" + dto.getUsuarioId();
            UsuarioDTO usuarioReal = restTemplate.getForObject(urlEspecifica, UsuarioDTO.class);
            
            if (usuarioReal == null) {
                throw new RuntimeException("El usuario con ID " + dto.getUsuarioId() + " no existe en el sistema.");
            }
            
            nuevaIncidencia.setUsuario(usuarioReal);
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Error: El usuario con ID " + dto.getUsuarioId() + " no fue encontrado en el microservicio Spaces.");
            
        } catch (Exception e) {
            UsuarioDTO usuarioMock = new UsuarioDTO();
            usuarioMock.setId(dto.getUsuarioId());
            usuarioMock.setNombre("Cliente de Spaces Offline (Fallback)");
            usuarioMock.setEmail("N/A");
            nuevaIncidencia.setUsuario(usuarioMock);
        }

        incidenciasMemoria.add(nuevaIncidencia);
        return nuevaIncidencia;
    }

    public List<IncidenciaResponseDTO> obtenerTodasConUsuarios() {
        return incidenciasMemoria;
    }
}