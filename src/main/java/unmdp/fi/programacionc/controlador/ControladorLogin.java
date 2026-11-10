package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.persistencia.ControlAcceso;
import unmdp.fi.programacionc.universo.Universo;
import unmdp.fi.programacionc.vista.IVistaLogin;
import unmdp.fi.programacionc.vista.VentanaRegistro;

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
            } else if (IVistaLogin.IR_A_REGISTRO.equals(evento.getActionCommand())) {
                abrirRegistro();
            }
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    /** Seleccion de vista: esconde el login y abre la vista de registro. Al volver, el login reaparece. */
    private void abrirRegistro() {
        vista.ocultar();
        VentanaRegistro registro = new VentanaRegistro();
        new ControladorRegistro(registro, controlAcceso, vista);
        registro.mostrar();
    }

    private void ingresar(String usuario, String contrasenia) {
        if (controlAcceso.iniciarSesion(usuario, contrasenia)) {
            vista.mostrarMensaje("Bienvenido/a, " + usuario);
            vista.cerrar();
            // Seleccion de vista: el login termino, se abre el panel de control con una sesion nueva
            new ControladorPrincipal(new Sesion(usuario, new Universo())).mostrar();
        } else {
            vista.mostrarError("Usuario o contrasenia incorrectos");
        }
    }
}