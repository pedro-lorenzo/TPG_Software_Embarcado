package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;

/**
 * Lo que el controlador puede pedirle a la vista de registro de usuarios.
 */
public interface IVistaRegistro {

    String CREAR_CUENTA = "CREAR_CUENTA";
    String VOLVER = "VOLVER";

    String getUsuario();
    String getContrasenia();
    String getConfirmacion();

    void mostrarMensaje(String mensaje);
    void mostrarError(String mensaje);

    void setActionListener(ActionListener listener);

    void mostrar();
    void cerrar();
}