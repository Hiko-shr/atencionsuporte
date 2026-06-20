package com.atencion.atencion.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.atencion.atencion.dto.IncidenciaRequestDTO;
import com.atencion.atencion.dto.IncidenciaResponseDTO;
import com.atencion.atencion.dto.UsuarioDTO;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IncidenSuportServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IncidenSuportService service;

    @BeforeEach
    void setUp() throws Exception {
        Field restTemplateField = IncidenSuportService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(service, restTemplate);
    }

    @Test
    void testCrearIncidenciaExitoso() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setUsuarioId(1L);
        dto.setDescripcion("Problema con la cuenta");
        dto.setPriority(1);

        UsuarioDTO usuarioMock = new UsuarioDTO();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Carlos");

        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(usuarioMock);

        IncidenciaResponseDTO result = service.crearIncidencia(dto);

        assertNotNull(result);
        assertEquals("Problema con la cuenta", result.getDescripcion());
        assertEquals("ABIERTO", result.getEstado());
        assertNotNull(result.getUsuario());
        assertEquals("Carlos", result.getUsuario().getNombre());
    }

    @Test
    void testCrearIncidenciaFaltaUsuarioId() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setDescripcion("Problema");
        dto.setPriority(1);

        assertThrows(RuntimeException.class, () -> service.crearIncidencia(dto));
    }

    @Test
    void testCrearIncidenciaDescripcionVacia() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setUsuarioId(1L);
        dto.setDescripcion("   ");
        dto.setPriority(1);

        assertThrows(RuntimeException.class, () -> service.crearIncidencia(dto));
    }

    @Test
    void testCrearIncidenciaPrioridadInvalida() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setUsuarioId(1L);
        dto.setDescripcion("Problema");
        dto.setPriority(0);

        assertThrows(RuntimeException.class, () -> service.crearIncidencia(dto));
    }

    @Test
    void testCrearIncidenciaUsuarioNoExistente() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setUsuarioId(99L);
        dto.setDescripcion("Problema");
        dto.setPriority(1);

        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(null);

        assertThrows(RuntimeException.class, () -> service.crearIncidencia(dto));
    }

    @Test
    void testCrearIncidenciaUsuarioNoEncontradoHttp() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setUsuarioId(99L);
        dto.setDescripcion("Problema");
        dto.setPriority(1);

        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(RuntimeException.class, () -> service.crearIncidencia(dto));
    }

    @Test
    void testCrearIncidenciaFallback() {
        IncidenciaRequestDTO dto = new IncidenciaRequestDTO();
        dto.setUsuarioId(2L);
        dto.setDescripcion("Problema");
        dto.setPriority(2);

        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class)))
                .thenThrow(new RuntimeException("Error de conexion"));

        IncidenciaResponseDTO result = service.crearIncidencia(dto);

        assertNotNull(result);
        assertEquals("Cliente de Spaces Offline (Fallback)", result.getUsuario().getNombre());
        assertEquals("N/A", result.getUsuario().getEmail());
    }

    @Test
    void testObtenerTodasConUsuarios() {
        List<IncidenciaResponseDTO> result = service.obtenerTodasConUsuarios();
        assertNotNull(result);
    }
}