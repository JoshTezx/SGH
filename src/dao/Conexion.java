package dao;

import java.sql.*;

public class Conexion {
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String usuario = "root";
	private static final String contrasenia = "Origami2025";
	
	public static Connection getConexion() throws SQLException {
		return DriverManager.getConnection(url, usuario, contrasenia);
	}
}
