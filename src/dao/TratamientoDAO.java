package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Tratamiento;

public class TratamientoDAO {
	private int idPorNombre(String sql, String nombreColumna, String nombre) {
		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nombre);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(nombreColumna);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int idMedicamento(String nombre) {
		return idPorNombre("SELECT id_medicamento FROM medicamento WHERE nombre = ?", "id_medicamento", nombre);
	}

	public int idTratamiento(String nombre) {
		return idPorNombre("SELECT id_tratamiento FROM tratamiento WHERE nombre = ?", "id_tratamiento", nombre);
	}
	
	public boolean create(Tratamiento t) {
		String insertTratamiento = "INSERT INTO tratamiento (nombre, duracion, reacciones_paciente) VALUES (?, ?, ?)";
		String insertRelacion = "INSERT INTO tratamiento_medicamento (id_tratamiento, id_medicamento) VALUES (?, ?)";

		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(insertTratamiento, Statement.RETURN_GENERATED_KEYS)) {			
			
			stmt.setString(1, t.getNombre());
			stmt.setInt(2, t.getDuracion());
			stmt.setString(3, t.getPosiblesReaccionesDelPaciente());
			stmt.executeUpdate();
			
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					int idTratamiento = rs.getInt(1);
					
					for (String medicamento : t.getMedicamentos()) {
						int idMedicamento = idMedicamento(medicamento);
						if(idMedicamento > 0) {
							try (PreparedStatement stmt2 = conn.prepareStatement(insertRelacion)) {
								stmt2.setInt(1, idTratamiento);
								stmt2.setInt(2, idMedicamento);
								stmt2.executeUpdate();
							}
						}
					}
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(Tratamiento nuevo, Tratamiento viejo) {
		String updateTratamiento = "UPDATE tratamiento SET nombre = ?, duracion = ?, reacciones_paciente = ? WHERE id_tratamiento = ?";
		String deleteRelaciones = "DELETE FROM tratamiento_medicamento WHERE id_tratamiento = ?";
		String insertRelacion = "INSERT INTO tratamiento_medicamento (id_tratamiento, id_medicamento) VALUES (?, ?)";
		
		try (Connection conn = Conexion.getConexion()) {
			
			int idTratamiento = idTratamiento(viejo.getNombre());
			
			try (PreparedStatement stmt = conn.prepareStatement(updateTratamiento)) {
				stmt.setString(1, nuevo.getNombre());
				stmt.setInt(2, nuevo.getDuracion());
				stmt.setString(3, nuevo.getPosiblesReaccionesDelPaciente());
				stmt.setInt(4, idTratamiento); 
				stmt.executeUpdate();
			}
			
			try (PreparedStatement stmt = conn.prepareStatement(deleteRelaciones)) {
				stmt.setInt(1, idTratamiento);
				stmt.executeUpdate();
			}

			for (String nombreMed : nuevo.getMedicamentos()) {
				int idMedicamento = idMedicamento(nombreMed);
				if (idMedicamento > 0) {
					try (PreparedStatement stmt = conn.prepareStatement(insertRelacion)) {
						stmt.setInt(1, idTratamiento);
						stmt.setInt(2, idMedicamento);
						stmt.executeUpdate();
					}
				}
			}
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(Tratamiento t) {
		String sql = "DELETE FROM tratamiento WHERE id_tratamiento = ?";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, idTratamiento(t.getNombre()));
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<String> readTratamientosAll() {
		ArrayList<String> nombres = new ArrayList<String>();

		String sql = "SELECT nombre FROM tratamiento";

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
	
	public ArrayList<Tratamiento> readAll() {
		ArrayList<Tratamiento> tratamientos = new ArrayList<>();
		
		String sql = "SELECT t.nombre AS nombre_tratamiento, t.duracion, t.reacciones_paciente, m.nombre AS nombre_medicamento FROM tratamiento t JOIN tratamiento_medicamento tm ON t.id_tratamiento = tm.id_tratamiento JOIN medicamento m ON tm.id_medicamento = m.id_medicamento;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);			
			ResultSet rs = stmt.executeQuery()) {
			
			String actual = "";
			Tratamiento t = null;
			
			while(rs.next()) {
				String tratamiento = rs.getString("nombre_tratamiento");
				
				if (!tratamiento.equals(actual)) {
					if (t != null) tratamientos.add(t);
					
					t = new Tratamiento();
					t.setNombre(tratamiento);
					t.setDuracion(rs.getInt("duracion"));
					t.setPosiblesReaccionesDelPaciente(rs.getString("reacciones_paciente"));
					t.setMedicamentos(new ArrayList<>());
					actual = tratamiento;
				}
				t.getMedicamentos().add(rs.getString("nombre_medicamento"));
			}
			if (t != null) tratamientos.add(t);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return tratamientos;
	}
}