package dao;

import java.sql.*;
import java.util.ArrayList;

import model.*;

public class CamaDAO {
	private int idCamaPorNumero(int numeroCama) {
		String sql = "SELECT id_cama FROM cama WHERE numero = ?;";
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, numeroCama);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) return rs.getInt("id_cama");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int idCama(Cama c) {
		return idCamaPorNumero(c.getNumero());
	}
	
	public int idCama(Admision a) {
		return idCamaPorNumero(a.getNumeroCama());
	}
	
	public boolean create(Cama c) {
		DepartamentoDAO dDAO = new DepartamentoDAO();
		
		String sql = "INSERT INTO cama (numero, estado, id_departamento) VALUES (?, ?, ?);";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement pStmt = conn.prepareStatement(sql)) {			
			
			pStmt.setInt(1, c.getNumero());
			pStmt.setString(2, c.getEstado().toString());
			pStmt.setInt(3, dDAO.idDepartamento(c));
			return pStmt.executeUpdate() == 1;
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(Cama nuevo, Cama viejo) {
		DepartamentoDAO dDAO = new DepartamentoDAO();
		
		String sql = "UPDATE cama SET numero = ?, estado = ?, id_departamento = ? WHERE id_cama = ?;";
		
		try (Connection conn = Conexion.getConexion();
				PreparedStatement stmt = conn.prepareStatement(sql)) {			
			
			stmt.setInt(1, nuevo.getNumero());
			stmt.setString(2, nuevo.getEstado().toString());
			stmt.setInt(3, dDAO.idDepartamento(nuevo));
			stmt.setInt(4, idCama(viejo));
			return stmt.executeUpdate() == 1;
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(Cama c) {
		String sql = "DELETE FROM cama WHERE id_cama = ?;";
		
		try (Connection conn = Conexion.getConexion();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, this.idCama(c));
			return stmt.executeUpdate() == 1;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Cama> readAll() {
		ArrayList<Cama> camas = new ArrayList<Cama>();
		
		String sql = "SELECT c.numero, c.estado, d.nombre AS nombre_departamento FROM cama c LEFT JOIN departamento d ON c.id_departamento = d.id_departamento;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {
			
			while(rs.next()) {
				Cama c = new Cama();
				c.setNumero(rs.getInt("numero"));
				c.setEstado(Estado.valueOf(rs.getString("estado")));
				c.setDepartamentoNombre(rs.getString("nombre_departamento"));
				camas.add(c);
			}
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return camas;
	}
	
	public ArrayList<Integer> readNumeroDeCamas() {
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		
		String sql = "SELECT numero FROM cama;";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {
			
			while(rs.next()) {
				numeros.add(rs.getInt("numero"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return numeros;
	}
	
	public ArrayList<Cama> readCamasDisponiblesConDepartamento() {
		ArrayList<Cama> disponibles = new ArrayList<>();
		
		String sql = "SELECT c.numero, d.nombre AS nombre_departamento FROM cama c LEFT JOIN departamento d ON c.id_departamento = d.id_departamento WHERE estado = 'Disponible';";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {
			
			while(rs.next()) {
				Cama c = new Cama();
				c.setNumero(rs.getInt("numero"));
				c.setDepartamentoNombre(rs.getString("nombre_departamento"));
				disponibles.add(c);
			}
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return disponibles;
	}
	
	public ArrayList<Integer> readCamasDisponiblesNumero() {
		ArrayList<Integer> disponibles = new ArrayList<>();

	    String sql = "SELECT numero FROM cama WHERE estado = 'Disponible';";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();) {
			
			while(rs.next()) {
				disponibles.add(rs.getInt("numero"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return disponibles;
	}
	
	public ArrayList<CamaOcupada> readCamasOcupadas() {
		ArrayList<CamaOcupada> ocupadas = new ArrayList<CamaOcupada>();
		
		String sql = "SELECT c.numero, d.nombre AS departamento, p.nombre, p.cuil FROM cama c LEFT JOIN departamento d ON c.id_departamento = d.id_departamento LEFT JOIN admision a ON c.id_cama = a.id_cama LEFT JOIN paciente p ON a.id_paciente = p.id_paciente WHERE estado = 'Ocupado';";
		
		try (Connection conn = Conexion.getConexion();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()){

			while(rs.next()) {
				CamaOcupada co = new CamaOcupada();
				co.setNumero(rs.getInt("numero"));
				co.setDepartamento(rs.getString("departamento"));
				co.setPaciente(rs.getString("nombre"));
				co.setCuil(rs.getString("cuil"));
				ocupadas.add(co);
			}
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return ocupadas;
	}
	
	public boolean cambiarEstadoCama(int numeroCama, Estado nuevoEstado) {
	    String sql = "UPDATE cama SET estado = ? WHERE numero = ?;";

	    try (Connection conn = Conexion.getConexion();
	    	PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, nuevoEstado.toString());
	        stmt.setInt(2, numeroCama);
	        return stmt.executeUpdate() == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    return false;
	}

}
