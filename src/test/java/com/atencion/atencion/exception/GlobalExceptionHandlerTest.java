package com.atencion.atencion.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testManejoErroresValidacion() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError error1 = new FieldError("objeto", "email", "El email es obligatorio");
        FieldError error2 = new FieldError("objeto", "prioridad", "La prioridad debe ser mayor a 0");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        Map<String, String> resultado = exceptionHandler.manejoErroresValidacion(ex);

        assertEquals(2, resultado.size());
        assertTrue(resultado.containsKey("email"));
        assertEquals("El email es obligatorio", resultado.get("email"));
        assertTrue(resultado.containsKey("prioridad"));
        assertEquals("La prioridad debe ser mayor a 0", resultado.get("prioridad"));
    }

    @Test
    void testManejoErroresNegocio() {
        RuntimeException ex = new RuntimeException("El usuario no existe en el sistema");
        
        Map<String, String> resultado = exceptionHandler.manejoErroresNegocio(ex);

        assertEquals(1, resultado.size());
        assertTrue(resultado.containsKey("error"));
        assertEquals("El usuario no existe en el sistema", resultado.get("error"));
    }
}