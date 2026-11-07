package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.persistencia.ControlAcceso;
import unmdp.fi.programacionc.vista.IVistaLogin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Atiende la pantalla de acceso: recibe el boton apretado, llama al modelo (ControlAcceso)
 * y le dice a la vista que mostrar. Conoce la vista solo por su interfaz.
 */
public class ControladorLogin implements ActionListener {

    private final IVistaLogin vista;
    private final ControlAcceso controlAcceso;

    /**
     * pre -> vista != null y controlAcceso != null
     */
    public ControladorLogin(IVistaLogin vista, ControlAcceso controlAcceso) {
        assert vista != null : "El controlador necesita una vista";
        assert controlAcceso != null : "El controlador necesita el control de acceso";
        this.vista = vista;
        this.controlAcceso = controlAcceso;
        this.vista.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String usuario = vista.getUsuario();
        String contrasenia = vista.getContrasenia();
        try {
            if (IVistaLogin.INGRESAR.equals(evento.getActionCommand())) {
                ingresar(usuario, contrasenia);
            } else if (IVistaLogin.REGISTRARSE.equals(evento.getActionCommand())) {
                controlAcceso.registrar(usuario, contrasenia);
                vista.mostrarMensaje("Usuario " + usuario + " registrado. Ya podes ingresar.");
            }
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void ingresar(String usuario, String contrasenia) {
        if (controlAcceso.iniciarSesion(usuario, contrasenia)) {
            vista.mostrarMensaje("Bienvenido/a, " + usuario);
            vista.cerrar();
            // PROXIMO PASO: abrir la ventana principal (seleccion de vista)
        } else {
            vista.mostrarError("Usuario o contrasenia incorrectos");
        }
    }
}