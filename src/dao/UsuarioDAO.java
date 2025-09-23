package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Rol;
import model.Usuario;

public class UsuarioDAO {

    public Usuario autenticar(String nombreUsuario, String contrasenia) {
        String sql = "SELECT nombre_usuario, contrasenia, rol FROM usuario WHERE nombre_usuario = ? AND contrasenia = ?";
        Usuario usuario = null;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasenia); 

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setUsuario(rs.getString("nombre_usuario"));
                    usuario.setContrasenia(rs.getString("contrasenia")); 
                    usuario.setRol(Rol.valueOf(rs.getString("rol")));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }
}
