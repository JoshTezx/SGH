package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Admision;

public class AdmisionDAO {
	
	
	public int idTratamiento(String nombre) {
		String sql = "SELECT id_tratamiento FROM tratamiento WHERE nombre = ?";
		
		try (Connection conn = Conexion.getConexion(); 
			PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nombre);
			try (ResultSet rs = stmt.executeQuery()){
				if(rs.next()) return rs.getInt("id_tratamiento");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public int idAdmision(String nombre) {		
		String sql = "SELECT a.id_admision FROM admision a JOIN paciente p ON a.id_paciente = p.id_paciente WHERE p.nombre = ?;";
		
		try (Connection conn = Conexion.getConexion(); 
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, nombre);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) return rs.getInt("id_admision");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean create(Admision a) {
		String insertAdmision = "INSERT INTO admision (id_paciente, fecha_ingreso, fecha_alta, id_cama) VALUES (?, ?, ?, ?);";
		String insertRelacion = "INSERT INTO admision_tratamiento (id_admision, id_tratamiento) VALUES (?, ?);";
		String actualizarCama = "UPDATE cama SET estado = ? WHERE id_cama = ?";
		
		try (Connection conn = Conexion.getConexion()){
			PacienteDAO pDAO = new PacienteDAO();
			CamaDAO cDAO = new CamaDAO();
			
			try (PreparedStatement pStmt = conn.prepareStatement(insertAdmision, Statement.RETURN_GENERATED_KEYS);) {								
								
				pStmt.setInt(1, pDAO.idPaciente(a));
				Date ingreso = Date.valueOf(a.getFechaIngreso());
				pStmt.setDate(2, ingreso);

				if (a.getFechaAlta() != null) {
				    pStmt.setDate(3, Date.valueOf(a.getFechaAlta()));
				} else {
				    pStmt.setNull(3, java.sql.Types.DATE);
				}

				pStmt.setInt(4, cDAO.idCama(a));
				pStmt.executeUpdate();
				
				try (ResultSet rs = pStmt.getGeneratedKeys()){
					if (rs.next()) {
						int idAdmision = rs.getInt(1);
						
						try (PreparedStatement stmtCama = conn.prepareStatement(actualizarCama)){
							stmtCama.setString(1, "Ocupado");
							stmtCama.setInt(2, cDAO.idCama(a));
							stmtCama.executeUpdate();
						}
						
						for (String tratamiento : a.getTratamientos()) {
							int idTratamiento = idTratamiento(tratamiento);
							if (idTratamiento > 0) {
								try	(PreparedStatement stmt2 = conn.prepareStatement(insertRelacion)){
									stmt2.setInt(1, idAdmision);
									stmt2.setInt(2, idTratamiento);
									stmt2.executeUpdate();
								}
							}
						}
						return true;
					}	
				}			
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}		
		return false;
	}
	
	public boolean update(Admision nuevo, Admision viejo) {
		String actualizarAdmision = "UPDATE admision SET id_paciente = ?, fecha_ingreso = ?, fecha_alta = ?, id_cama = ? WHERE id_admision = ?;";
		String actualizarCama = "UPDATE cama SET estado = ? WHERE id_cama = ?";
		
		try (Connection conn = Conexion.getConexion()) {			
			PacienteDAO pDAO = new PacienteDAO();
			CamaDAO cDAO = new CamaDAO();
			int idAdmision = idAdmision(viejo.getNombrePaciente());
			
			try (PreparedStatement pStmt = conn.prepareStatement(actualizarAdmision)) {
				pStmt.setInt(1, pDAO.idPaciente(nuevo));
				pStmt.setDate(2, Date.valueOf(nuevo.getFechaIngreso()));
				
				if (nuevo.getFechaAlta() != null) {
				    pStmt.setDate(3, Date.valueOf(nuevo.getFechaAlta()));
				} else {
				    pStmt.setNull(3, Types.DATE);
				}
				
				pStmt.setInt(4, cDAO.idCama(nuevo));
				pStmt.setInt(5, idAdmision);
				pStmt.executeUpdate();
			}
			
			String estadoCama = (nuevo.getFechaAlta() != null) ? "Disponible" : "Ocupado";
			try (PreparedStatement stmtCama = conn.prepareStatement(actualizarCama)) {
				stmtCama.setString(1, estadoCama);
				stmtCama.setInt(2, cDAO.idCama(nuevo));
				stmtCama.executeUpdate();
			}
			
			try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM admision_tratamiento WHERE id_admision = ?;")) {
				deleteStmt.setInt(1, idAdmision);
				deleteStmt.executeUpdate();
			}
			
			for (String tratamiento : nuevo.getTratamientos()) {
				int idTratamiento = idTratamiento(tratamiento);
				if (idTratamiento > 0) {
					try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO admision_tratamiento (id_admision, id_tratamiento) VALUES (?, ?);");) {
						insertStmt.setInt(1, idAdmision);
						insertStmt.setInt(2, idTratamiento);
						insertStmt.executeUpdate();
					}		
				}
			}
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(Admision a) {
		String updateCama = "UPDATE cama SET estado = ? WHERE id_cama = ?";
		String deleteRel = "DELETE FROM admision_tratamiento WHERE id_admision = ?";
		String deleteAdm = "DELETE FROM admision WHERE id_admision = ?";
		
		try (Connection conn = Conexion.getConexion()) {
			CamaDAO cDAO = new CamaDAO();			
			int idAdmision = idAdmision(a.getNombrePaciente());
			int idCama = cDAO.idCama(a);
			
			try (PreparedStatement updateStmt = conn.prepareStatement(updateCama)) {
				updateStmt.setString(1, "Disponible");
				updateStmt.setInt(2, idCama);
				updateStmt.executeUpdate();
			}
	        
			try (PreparedStatement deleteRelacion = conn.prepareStatement(deleteRel)) {
				deleteRelacion.setInt(1, idAdmision);
				deleteRelacion.executeUpdate();
			}
	        
	        try (PreparedStatement pStmt = conn.prepareStatement(deleteAdm)) {
	        	pStmt.setInt(1, idAdmision);
	        	return pStmt.executeUpdate() == 1;
	        }
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Admision> readAll() {
		ArrayList<Admision> admisiones = new ArrayList<>();
		
		String url = "jdbc:mysql://localhost:3306/hospital";
		String usuario = "root";
		String contrasenia = "Origami2025";
		Connection conn = null;
		
		String sql = "SELECT a.id_admision, p.nombre AS nombre_paciente, a.fecha_ingreso, a.fecha_alta, c.id_cama AS numero_cama, t.nombre AS nombre_tratamiento FROM admision a JOIN paciente p ON a.id_paciente = p.id_paciente JOIN cama c ON a.id_cama = c.id_cama JOIN admision_tratamiento atm ON a.id_admision = atm.id_admision JOIN tratamiento t ON atm.id_tratamiento = t.id_tratamiento;";
		
		try {
			conn = DriverManager.getConnection(url, usuario, contrasenia);

			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			int actual = -1;
			Admision a = null;
			
			while(rs.next()) {
				int idAdmision = rs.getInt("id_admision");
				
				if (idAdmision != actual) {
					if (a != null) admisiones.add(a);
					
					a = new Admision();
					a.setNombrePaciente(rs.getString("nombre_paciente"));
					a.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
					
					Date fechaAlta = rs.getDate("fecha_alta");
					a.setFechaAlta(fechaAlta != null ? fechaAlta.toLocalDate() : null);
					
					/*
					Date fechaAltaDate = rs.getDate("fecha_alta");
					if (fechaAltaDate != null) {
						a.setFechaAlta(fechaAltaDate.toLocalDate());
					} else {
						a.setFechaAlta(null);
					}
					*/
					
					a.setNumeroCama(rs.getInt("numero_cama"));
					a.setTratamientos(new ArrayList<>());		
					actual = idAdmision;
				}
				a.getTratamientos().add(rs.getString("nombre_tratamiento"));
			}
			if (a != null) admisiones.add(a);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return admisiones;
	}
}
