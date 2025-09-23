package dao;

import java.sql.*;
import java.util.ArrayList;

import model.*;

public class DepartamentoDAO {
	private int idDepartamentoPorNombre(String nombre) {
		String sql = "SELECT id_departamento FROM departamento WHERE nombre = ?";
        
		try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id_departamento");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
	}
	
	public int idDepartamento(Departamento d) {
		return idDepartamentoPorNombre(d.getNombre());
	}
	
	public int idDepartamento(Doctor d) {
		return idDepartamentoPorNombre(d.getDepartamento());
	}
	
	public int idDepartamento(Cama c) {
		return idDepartamentoPorNombre(c.getDepartamentoNombre());
	}
	
	public int idDepartamentoNoExistente(Departamento d) {
	    String sqlBuscar = "SELECT id_departamento, camas_disponibles FROM departamento WHERE nombre = ?";
	    
	    try (Connection conn = Conexion.getConexion();
		    PreparedStatement psBuscar = conn.prepareStatement(sqlBuscar)) {

	        psBuscar.setString(1, d.getNombre());
	        try (ResultSet rs = psBuscar.executeQuery()) {
	        	if (rs.next()) {
		            int idDepartamento = rs.getInt("id_departamento");
		            int camasActuales = rs.getInt("camas_disponibles");
		            
		            if (d.getCamasDisponibles() > camasActuales) {
		            	int camasExtra = d.getCamasDisponibles() - camasActuales;
		                String updateDepto = "UPDATE departamento SET camas_disponibles = ? WHERE id_departamento = ?";
		                try (PreparedStatement psUpdate = conn.prepareStatement(updateDepto)) {
		                	psUpdate.setInt(1, d.getCamasDisponibles());
			                psUpdate.setInt(2, idDepartamento);
			                psUpdate.executeUpdate();
			                createCamas(idDepartamento, camasExtra);
		                }
		            }
		            return idDepartamento;
		        } else {
		            create(d); 
		            return idDepartamentoPorNombre(d.getNombre()); 
		        }
	        }  
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}

	
	public boolean create(Departamento d) {		
		String sql = "INSERT INTO departamento (nombre, ubicacion, id_doctor, camas_disponibles, camas_ocupadas) VALUES (?, ?, ?, ?, ?);";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			DoctorDAO dDAO = new DoctorDAO();
			stmt.setString(1, d.getNombre());
			stmt.setString(2, d.getUbicacion());
			if (d.getNombreMedico() == null || d.getNombreMedico().equals("Sin doctor")) {
			    stmt.setNull(3, Types.INTEGER);
			} else {
			    stmt.setInt(3, dDAO.idDoctor(d));
			}
			stmt.setInt(4, d.getCamasDisponibles());
			stmt.setInt(5, d.getCamasOcupadas());
			
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(Departamento nuevo, Departamento viejo) {
		String sql = "UPDATE departamento SET nombre = ?, ubicacion = ?, id_doctor = ?, camas_disponibles = ?, camas_ocupadas = ? WHERE id_departamento = ?;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			DoctorDAO dDAO = new DoctorDAO();
			stmt.setString(1, nuevo.getNombre());
			stmt.setString(2, nuevo.getUbicacion());
			if (nuevo.getNombreMedico() == null) {
			    stmt.setNull(3, Types.INTEGER);
			} else {
			    stmt.setInt(3, dDAO.idDoctor(nuevo));
			}
			stmt.setInt(4, nuevo.getCamasDisponibles());
			stmt.setInt(5, nuevo.getCamasOcupadas());
			stmt.setInt(6, idDepartamentoPorNombre(viejo.getNombre()));
			
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(Departamento d) {
		String sql = "DELETE FROM departamento WHERE id_departamento = ?;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, idDepartamentoPorNombre(d.getNombre()));
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Departamento> readAll() {
		ArrayList<Departamento> departamentos = new ArrayList<Departamento>();
		
		String sql = "SELECT p.id_departamento, p.nombre, p.ubicacion, d.nombre AS nombre_doctor, p.camas_disponibles FROM departamento p LEFT JOIN doctor d ON p.id_doctor = d.id_doctor;";

		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {

			while(rs.next()) {
				Departamento d = new Departamento();
				int idDepartamento = rs.getInt("id_departamento");
				d.setNombre(rs.getString("nombre"));
				d.setUbicacion(rs.getString("ubicacion"));
				d.setNombreMedico(rs.getString("nombre_doctor"));
				d.setCamasDisponibles(rs.getInt("camas_disponibles"));
				d.setCamasOcupadas(cantCamasOcupadas(idDepartamento));
				departamentos.add(d);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return departamentos;
	}
	
	public ArrayList<String> readNameDepartamento() {
		ArrayList<String> nombres = new ArrayList<>();
		
		String sql = "SELECT nombre FROM departamento";
		
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
	
	public ArrayList<Departamento> disponiblesPorDepartamento() {
	    ArrayList<Departamento> disponibles = new ArrayList<>();

        String sql = "SELECT nombre, camas_disponibles FROM departamento;";
	    
	    try (Connection conn = Conexion.getConexion();
	    	PreparedStatement stmt = conn.prepareStatement(sql);
		    ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Departamento d = new Departamento();
	            d.setNombre(rs.getString("nombre"));
	            d.setCamasDisponibles(rs.getInt("camas_disponibles"));
	            disponibles.add(d);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    return disponibles;
	}
	
	public void createCamas(int idDepartamento, int cantidad) {
		String sqlMax = "SELECT MAX(numero) FROM cama";
		String sqlInsert = "INSERT INTO cama (numero, estado, id_departamento) VALUES (?, 'Disponible', ?)";
		
	    try (Connection conn = Conexion.getConexion();
	    	PreparedStatement psMax = conn.prepareStatement(sqlMax);
	    	ResultSet rs = psMax.executeQuery()) {
	    	
	        int maxNumero = rs.next() ? rs.getInt(1) : 0;
	        
	        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
	        	for (int i = 1; i <= cantidad; i++) {
		            ps.setInt(1, maxNumero + i);
		            ps.setInt(2, idDepartamento);
		            ps.addBatch();
		        }
		        ps.executeBatch();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public int cantCamasOcupadas(int idDepartamento) {
		String sql = "SELECT COUNT(*) FROM cama WHERE estado = 'Ocupado' AND id_departamento = ?;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)) {			
			
			stmt.setInt(1, idDepartamento);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
