package dao;

import java.sql.*;
import java.util.ArrayList;

import model.Medicamento;

public class MedicamentoDAO {

	public int idMedicamento(Medicamento m) {
		String sql = "SELECT id_medicamento FROM medicamento WHERE nombre = ?";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setString(1, m.getNombreDroga());
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id_medicamento");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean create(Medicamento m) {
		String sql = "INSERT INTO medicamento (nombre, fabricacion, vencimiento, tipo) VALUES (?, ?, ?, ?)";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setString(1, m.getNombreDroga());
			pStmt.setDate(2, Date.valueOf(m.getFabricacion()));
			pStmt.setDate(3, Date.valueOf(m.getVencimiento()));
			pStmt.setString(4, m.getTipo());

			return pStmt.executeUpdate() == 1;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(Medicamento nuevo, Medicamento viejo) {
		String sql = "UPDATE medicamento SET nombre = ?, fabricacion = ?, vencimiento = ?, tipo = ? WHERE id_medicamento = ?";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setString(1, nuevo.getNombreDroga());
			pStmt.setDate(2, Date.valueOf(nuevo.getFabricacion()));
			pStmt.setDate(3, Date.valueOf(nuevo.getVencimiento()));
			pStmt.setString(4, nuevo.getTipo());
			pStmt.setInt(5, this.idMedicamento(viejo));

			return pStmt.executeUpdate() == 1;

		} catch (SQLException e) {
			e.printStackTrace();		
		}
		return false;
	}

	public boolean delete(Medicamento m) {
		String sql = "DELETE FROM medicamento WHERE id_medicamento = ?";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setInt(1, this.idMedicamento(m));
			return pStmt.executeUpdate() == 1;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<String> readMedicamentosName() {
		ArrayList<String> nombresMedicamentos = new ArrayList<>();
		String sql = "SELECT nombre FROM medicamento";

		try (Connection conn = Conexion.getConexion();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				nombresMedicamentos.add(rs.getString("nombre"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nombresMedicamentos;
	}

	public ArrayList<Medicamento> readAll() {
		ArrayList<Medicamento> medicamentos = new ArrayList<>();
		String sql = "SELECT nombre, fabricacion, vencimiento, tipo FROM medicamento";

		try (Connection conn = Conexion.getConexion();
		     PreparedStatement stmt = conn.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Medicamento m = new Medicamento();
				m.setNombreDroga(rs.getString("nombre"));
				m.setFabricacion(rs.getDate("fabricacion").toLocalDate());
				m.setVencimiento(rs.getDate("vencimiento").toLocalDate());
				m.setTipo(rs.getString("tipo"));
				medicamentos.add(m);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return medicamentos;
	}
}
