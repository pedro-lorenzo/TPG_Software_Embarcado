package unmdp.fi.programacionc.persistencia;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

public class ControlAcceso {

    private final UsuarioDAO dao;

    public ControlAcceso(UsuarioDAO dao) {
        this.dao = dao;
    }

    public void registrar(String nombreUsuario, String contrasenia) {
        if (dao.buscar(nombreUsuario) != null) {
            throw new OperacionInvalidaException("Ya existe un usuario con el nombre " + nombreUsuario);
        }
        dao.guardar(new UsuarioDTO(nombreUsuario, contrasenia));
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasenia) {
        UsuarioDTO usuario = dao.buscar(nombreUsuario);
        if (usuario == null) {
            return false;
        }
        return usuario.getContrasenia().equals(contrasenia);
    }
}
