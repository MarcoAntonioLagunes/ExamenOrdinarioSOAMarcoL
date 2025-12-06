-- Script de base de datos para el servicio SOAP de Alumnos
-- Crea la base de datos `uav` y la tabla `alumnos` compatible con soap_alumnos.py

CREATE DATABASE IF NOT EXISTS uav
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE uav;

CREATE TABLE IF NOT EXISTS alumnos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    edad INT NOT NULL,
    correo VARCHAR(150) NOT NULL,
    telefono VARCHAR(50) NOT NULL
);
