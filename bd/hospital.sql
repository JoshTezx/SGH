DROP SCHEMA IF EXISTS hospital;

CREATE SCHEMA IF NOT EXISTS hospital;
USE hospital;

DROP TABLE IF EXISTS usuario;
CREATE TABLE IF NOT EXISTS usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario VARCHAR(45) NOT NULL UNIQUE,
  contrasenia VARCHAR(255) NOT NULL,
  rol ENUM('Administrador', 'Recepcionista', 'Doctor', 'Farmacéutico') NOT NULL
);

INSERT INTO `usuario` (`nombre_usuario`, `contrasenia`, `rol`) VALUES 
('admin', '1234', 'Administrador'),
('recepcion', '1234', 'Recepcionista'),
('juan', '1234', 'Doctor'),
('farmacia', '1234', 'Farmacéutico');

 DROP TABLE IF EXISTS departamento;
CREATE TABLE IF NOT EXISTS departamento (
  id_departamento INT AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  ubicacion VARCHAR(100) NOT NULL,
  id_doctor INT,
  camas_disponibles INT CHECK (camas_disponibles >= 0),
  camas_ocupadas INT CHECK (camas_ocupadas >= 0),
  PRIMARY KEY (id_departamento));  

DROP TABLE IF EXISTS doctor;
CREATE TABLE IF NOT EXISTS doctor (
  id_doctor INT AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  matricula INT NOT NULL UNIQUE,
  id_departamento INT,
  PRIMARY KEY (id_doctor),
  FOREIGN KEY (id_departamento) REFERENCES departamento(id_departamento) ON DELETE SET NULL ON UPDATE CASCADE);
  
  ALTER TABLE departamento
  ADD CONSTRAINT fk_departamento_doctor
  FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor) ON DELETE SET NULL ON UPDATE CASCADE;
  
DROP TABLE IF EXISTS paciente;
CREATE TABLE IF NOT EXISTS paciente (
  id_paciente INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(150) NOT NULL,
  sexo ENUM('Masculino', 'Femenino') NOT NULL,
  cuil VARCHAR(20) NOT NULL,
  PRIMARY KEY (id_paciente));

DROP TABLE IF EXISTS cama;
CREATE TABLE IF NOT EXISTS cama (
  id_cama INT AUTO_INCREMENT,
  numero INT NOT NULL,
  estado ENUM('Disponible', 'Ocupado') NOT NULL DEFAULT 'Disponible',
  id_departamento INT NULL,
  PRIMARY KEY (id_cama),
  FOREIGN KEY (id_departamento) REFERENCES departamento (id_departamento) ON DELETE CASCADE ON UPDATE CASCADE);
  
DROP TABLE IF EXISTS tratamiento;
CREATE TABLE IF NOT EXISTS tratamiento (
  id_tratamiento INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  duracion INT NOT NULL,
  reacciones_paciente VARCHAR(500),
  PRIMARY KEY (id_tratamiento));
  
DROP TABLE IF EXISTS admision;
CREATE TABLE IF NOT EXISTS admision (
  id_admision INT AUTO_INCREMENT,
  id_paciente INT NOT NULL,
  fecha_ingreso DATE NOT NULL,
  fecha_alta DATE DEFAULT NULL,
  id_cama INT NOT NULL,
  PRIMARY KEY (id_admision),
  FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente),
  FOREIGN KEY (id_cama) REFERENCES cama (id_cama));

DROP TABLE IF EXISTS admision_tratamiento;
CREATE TABLE IF NOT EXISTS admision_tratamiento (
  id_admision INT NOT NULL,
  id_tratamiento INT NOT NULL,
  PRIMARY KEY (id_admision, id_tratamiento),
  FOREIGN KEY (id_admision) REFERENCES admision (id_admision),
  FOREIGN KEY (id_tratamiento) REFERENCES tratamiento (id_tratamiento)
);

DROP TABLE IF EXISTS medicamento;
CREATE TABLE IF NOT EXISTS medicamento (
  id_medicamento INT AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  fabricacion DATE NOT NULL,
  vencimiento DATE NOT NULL,
  tipo VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_medicamento));

DROP TABLE IF EXISTS tratamiento_medicamento;
CREATE TABLE IF NOT EXISTS tratamiento_medicamento (
  id_tratamiento INT NOT NULL,
  id_medicamento INT NOT NULL,
  PRIMARY KEY (id_tratamiento, id_medicamento),
  FOREIGN KEY (id_tratamiento) REFERENCES tratamiento(id_tratamiento) ON DELETE CASCADE,
  FOREIGN KEY (id_medicamento) REFERENCES medicamento(id_medicamento) ON DELETE CASCADE
);
