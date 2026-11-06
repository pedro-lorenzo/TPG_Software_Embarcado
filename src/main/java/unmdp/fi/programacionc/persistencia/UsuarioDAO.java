package unmdp.fi.programacionc.persistencia;

public interface UsuarioDAO {

    void guardar(UsuarioDTO usuario);

    /** Devuelve el usuario o null si no existe. */
    UsuarioDTO buscar(String nombreUsuario);
}
