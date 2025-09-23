package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Departamento;
import model.Doctor;

public class DoctorDAO {
	private final DepartamentoDAO departamentoDAO = new DepartamentoDAO();

	public int idDoctorPorNombre(String nombre) {
		String sql = "SELECT id_doctor FROM doctor WHERE nombre = ?";
		
		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, nombre);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id_doctor");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int idDoctor(Doctor d) {
		return idDoctorPorNombre(d.getNombre());
	}

	public int idDoctor(Departamento d) {
		return idDoctorPorNombre(d.getNombreMedico());
	}

	public boolean create(Doctor d) {
		String sql = "INSERT INTO doctor (nombre, apellido, matricula, id_departamento) VALUES (?, ?, ?, ?)";
		
		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, d.getNombre());
			stmt.setString(2, d.getApellido());
			stmt.setInt(3, d.getMatricula());

			if (d.getDepartamento() == null || d.getDepartamento().equals("Sin departamento")) {
				stmt.setNull(4, Types.INTEGER);
			} else {
				stmt.setInt(4, departamentoDAO.idDepartamento(d));
			}

			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(Doctor nuevo, Doctor viejo) {
		String sql = "UPDATE doctor SET nombre = ?, apellido = ?, matricula = ?, id_departamento = ? WHERE id_doctor = ?";
		
		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nuevo.getNombre());
			stmt.setString(2, nuevo.getApellido());
			stmt.setInt(3, nuevo.getMatricula());

			if (nuevo.getDepartamento() == null || nuevo.getDepartamento().equals("Sin departamento")) {
				stmt.setNull(4, Types.INTEGER);
			} else {
				stmt.setInt(4, departamentoDAO.idDepartamento(nuevo));
			}
			stmt.setInt(5, idDoctor(viejo));

			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(Doctor d) {
		String sql = "DELETE FROM doctor WHERE id_doctor = ?";
		
		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idDoctor(d));
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Doctor> readAll() {
		ArrayList<Doctor> doctores = new ArrayList<>();
		
		String sql = "SELECT d.nombre, d.apellido, d.matricula, p.nombre AS nombre_departamento FROM doctor d LEFT JOIN departamento p ON d.id_departamento = p.id_departamento;";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Doctor d = new Doctor();
				d.setNombre(rs.getString("nombre"));
				d.setApellido(rs.getString("apellido"));
				d.setMatricula(rs.getInt("matricula"));
				d.setDepartamento(rs.getString("nombre_departamento"));
				doctores.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return doctores;
	}

	public ArrayList<String> readNombreDoctores() {
		ArrayList<String> nombres = new ArrayList<>();
		
		String sql = "SELECT nombre FROM doctor";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				nombres.add(rs.getString("nombre"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nombres;
	}
}
