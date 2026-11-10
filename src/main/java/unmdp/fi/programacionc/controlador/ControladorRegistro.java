package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.persistencia.ControlAcceso;
import unmdp.fi.programacionc.vista.IVistaLogin;
import unmdp.fi.programacionc.vista.IVistaRegistro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Atiende la vista de Registro: da de alta el usuario con ControlAcceso (que aplica las reglas
 * y lo guarda con el DAO) y, al terminar o al volver, regresa al login.
 * Conoce las dos vistas solo por sus interfaces.
 */
public class ControladorRegistro implements ActionListener {

    private final IVistaRegistro vista;
    private final ControlAcceso controlAcceso;
    private final IVistaLogin login;

    /**
     * pre -> vista, controlAcceso y login != null
     */
    public ControladorRegistro(IVistaRegistro vista, ControlAcceso controlAcceso, IVistaLogin login) {
        assert vista != null && controlAcceso != null && login != null : "Faltan piezas del registro";
        this.vista = vista;
        this.controlAcceso = controlAcceso;
        this.login = login;
        vista.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (IVistaRegistro.VOLVER.equals(evento.getActionCommand())) {
            volverAlLogin(null);
        } else if (IVistaRegistro.CREAR_CUENTA.equals(evento.getActionCommand())) {
            crearCuenta();
        }
    }

    private void crearCuenta() {
        String usuario = vista.getUsuario();
        String contrasenia = vista.getContrasenia();
        // Que las dos contrasenias coincidan es un control del formulario, no una regla del modelo
        if (!contrasenia.equals(vista.getConfirmacion())) {
            vista.mostrarError("Las contrasenias no coinciden");
            return;
        }
        try {
            controlAcceso.registrar(usuario, contrasenia);   // vacio, largo o repetido: lo rechaza el modelo
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
            return;
        }
        vista.mostrarMensaje("Usuario " + usuario + " registrado. Ya podes ingresar.");
        volverAlLogin(usuario);
    }

    /** Seleccion de vista: cierra el registro y vuelve a mostrar el login (con el usuario nuevo, si hay). */
    private void volverAlLogin(String usuarioNuevo) {
        vista.cerrar();
        if (usuarioNuevo != null) {
            login.setUsuario(usuarioNuevo);
        }
        login.mostrar();
    }
}