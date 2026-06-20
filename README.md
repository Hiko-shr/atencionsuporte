-- Crear la base de datos para Atencion
CREATE DATABASE IF NOT EXISTS atencion_db;
USE atencion_db;

-- Crear tabla de Incidencias
CREATE TABLE incidencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'ABIERTO',
    prioridad INT NOT NULL,
    fecha_reporte DATETIME NOT NULL,
    usuario_id BIGINT NOT NULL -- Almacena el ID del usuario remoto sin FK física
);

-- Insertar una incidencia de prueba vinculada al usuario ID 1
INSERT INTO incidencias (descripcion, estado, prioridad, fecha_reporte, usuario_id)
VALUES ('El sistema se queda congelado al cargar el perfil', 'ABIERTO', 3, NOW(), 1);