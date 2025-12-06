-- Script de base de datos para la API REST de Cursos
-- Compatible con mx.uav.cursos.model.Curso y application.properties (BD `uav`, tabla `cursos`)

CREATE DATABASE IF NOT EXISTS uav
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE uav;

CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    fecha_inicio DATE,
    profesor VARCHAR(255)
);
