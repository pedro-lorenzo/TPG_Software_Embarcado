package unmdp.fi.programacionc.persistencia;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

/**
 * Reglas del acceso al sistema (modelo). No sabe nada de pantallas.
 */
public class ControlAcceso {

    /** Coincide con el VARCHAR(50) de database/schema.sql */
    public static final int LARGO_MAXIMO = 50;

    private final UsuarioDAO dao;

    public ControlAcceso(UsuarioDAO dao) {
        this.dao = dao;
    }

    /**
     * @throws OperacionInvalidaException si algun dato esta vacio, es muy largo o el usuario ya existe
     */
    public void registrar(String nombreUsuario, String contrasenia) {
        validar(nombreUsuario, "nombre de usuario");
        validar(contrasenia, "contrasenia");
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

    private void validar(String dato, String nombreDelDato) {
        if (dato == null || dato.trim().isEmpty()) {
            throw new OperacionInvalidaException("El " + nombreDelDato + " no puede estar vacio");
        }
        if (dato.length() > LARGO_MAXIMO) {
            throw new OperacionInvalidaException("El " + nombreDelDato + " no puede superar "
                    + LARGO_MAXIMO + " caracteres");
        }
    }
}