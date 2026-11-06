package unmdp.fi.programacionc.persistencia;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

public class UsuarioDAOJdbc implements UsuarioDAO {

    private static final String URL = "jdbc:h2:./data/tpg";
    private static final String RUTA_SCHEMA = "database/schema.sql";

    public UsuarioDAOJdbc() {
        crearTabla();
    }

    // Ejecuta database/schema.sql (tiene IF NOT EXISTS, se puede correr siempre)
    private void crearTabla() {
        try (Connection conexion = DriverManager.getConnection(URL);
             Statement sentencia = conexion.createStatement()) {
            String sql = Files.readString(Path.of(RUTA_SCHEMA));
            sentencia.execute(sql);
        } catch (Exception e) {
            throw new OperacionInvalidaException("No se pudo crear la tabla de usuarios: " + e.getMessage());
        }
    }

    @Override
    public void guardar(UsuarioDTO usuario) {
        String sql = "INSERT INTO usuario (nombre_usuario, contrasenia) VALUES (?, ?)";
        try (Connection conexion = DriverManager.getConnection(URL);
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombreUsuario());
            sentencia.setString(2, usuario.getContrasenia());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new OperacionInvalidaException("No se pudo guardar el usuario: " + e.getMessage());
        }
    }

    @Override
    public UsuarioDTO buscar(String nombreUsuario) {
        String sql = "SELECT nombre_usuario, contrasenia FROM usuario WHERE nombre_usuario = ?";
        try (Connection conexion = DriverManager.getConnection(URL);
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nombreUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new UsuarioDTO(resultado.getString("nombre_usuario"),
                            resultado.getString("contrasenia"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new OperacionInvalidaException("No se pudo consultar el usuario: " + e.getMessage());
        }
    }
}