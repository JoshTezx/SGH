package dao;

import java.sql.*;
import java.util.ArrayList;

import model.*;

public class PacienteDAO {
	public int idPaciente(Paciente p) {
		String sql = "SELECT id_paciente FROM paciente WHERE nombre = ?";
		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, p.getNombre());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) return rs.getInt("id_paciente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int idPaciente(Admision a) {
		String sql = "SELECT id_paciente FROM paciente WHERE nombre = ?;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setString(1, a.getNombrePaciente());
			try (ResultSet rs = pStmt.executeQuery()) {
				if(rs.next()) return rs.getInt("id_paciente");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean create(Paciente p) {
		String sql = "INSERT INTO hospital.paciente (nombre, direccion, sexo, cuil) VALUES (?, ?, ?, ?);";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, p.getNombre());
			stmt.setString(2, p.getDireccion());
			stmt.setString(3, p.getSexo().toString());
			stmt.setString(4, p.getCuil());	
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(Paciente nuevo, Paciente viejo) {
		String sql = "UPDATE paciente SET nombre = ?, direccion = ?, sexo = ?, cuil = ? WHERE id_paciente = ?;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setString(1, nuevo.getNombre());
			stmt.setString(2, nuevo.getDireccion());
			stmt.setString(3, nuevo.getSexo().toString());
			stmt.setString(4, nuevo.getCuil());
			stmt.setInt(5, idPaciente(viejo));
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(Paciente p) {
		String sql = "DELETE FROM paciente WHERE id_paciente = ?;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)) {			
			
			stmt.setInt(1, idPaciente(p));
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<String> readPacientesAll() {
		ArrayList<String> nombres = new ArrayList<>();

		String sql = "SELECT nombre FROM paciente;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {			
			
			
			while(rs.next()) {
				nombres.add(rs.getString("nombre"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return nombres;
	}
	
	public ArrayList<Paciente> readAll() {
		ArrayList<Paciente> pacientes = new ArrayList<>();

		String sql = "SELECT nombre, direccion, sexo, cuil FROM paciente;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {

			while(rs.next()) {
				Paciente p = new Paciente();
				p.setNombre(rs.getString("nombre"));
				p.setDireccion(rs.getString("direccion"));
				p.setSexo(Sexo.valueOf(rs.getString("sexo")));
				p.setCuil(rs.getString("cuil"));
				pacientes.add(p);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return pacientes;
	}
	
	public ArrayList<ReportePaciente> readPacientesDetalles() {
		ArrayList<ReportePaciente> pacientes = new ArrayList<>();
		
		String sql = """
			SELECT 
				p.nombre AS nombre_paciente,
				a.fecha_ingreso,
				a.fecha_alta,
				GROUP_CONCAT(DISTINCT t.nombre SEPARATOR ', ') AS tratamientos,
				GROUP_CONCAT(DISTINCT m.nombre SEPARATOR ', ') AS medicamentos
			FROM paciente p
			JOIN admision a ON p.id_paciente = a.id_paciente
			LEFT JOIN admision_tratamiento at ON a.id_admision = at.id_admision
			LEFT JOIN tratamiento t ON at.id_tratamiento = t.id_tratamiento
			LEFT JOIN tratamiento_medicamento tm ON t.id_tratamiento = tm.id_tratamiento
			LEFT JOIN medicamento m ON tm.id_medicamento = m.id_medicamento
			GROUP BY p.nombre, a.fecha_ingreso, a.fecha_alta;
		""";

		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				ReportePaciente rp = new ReportePaciente();
				rp.setNombre(rs.getString("nombre_paciente"));
				rp.setFechaAlta(rs.getDate("fecha_ingreso").toLocalDate());
				
				Date fechaBaja = rs.getDate("fecha_alta");
				rp.setFechaBaja(fechaBaja != null ? fechaBaja.toLocalDate() : null);
				
				rp.setTratamientos(rs.getString("tratamientos") != null ? rs.getString("tratamientos") : "Sin tratamientos");
				rp.setDrogasSuminstradas(rs.getString("medicamentos") != null ? rs.getString("medicamentos") : "Sin medicamentos");
				pacientes.add(rp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return pacientes;
	}

	
	public ArrayList<PacienteConDepartamentos> readPacientesConVariosDepartamentos() {
		ArrayList<PacienteConDepartamentos> pacientes = new ArrayList<>();

		String sql = """
			SELECT 
				p.nombre AS nombre_paciente,
				p.cuil,
				COUNT(DISTINCT d.id_departamento) AS cantidad_departamentos
			FROM paciente p
			JOIN admision a ON p.id_paciente = a.id_paciente
			JOIN admision_tratamiento at ON a.id_admision = at.id_admision
			JOIN cama c ON a.id_cama = c.id_cama
			JOIN departamento d ON c.id_departamento = d.id_departamento
			GROUP BY p.id_paciente, p.nombre, p.cuil
			HAVING COUNT(DISTINCT d.id_departamento) > 1;
		""";

		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {			

			while (rs.next()) {
				PacienteConDepartamentos pcd = new PacienteConDepartamentos(
						rs.getString("nombre_paciente"),
						rs.getString("cuil"),
						rs.getInt("cantidad_departamentos")
				);
				pacientes.add(pcd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pacientes;
	}
}