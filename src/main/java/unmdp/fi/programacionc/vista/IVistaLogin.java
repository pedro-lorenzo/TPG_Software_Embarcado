package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;

/**
 * Lo que el controlador puede pedirle a la pantalla de acceso.
 * El controlador conoce esta interfaz, nunca la ventana concreta.
 */
public interface IVistaLogin {

    // Comandos de los botones: el controlador los usa para saber cual se apreto
    String INGRESAR = "INGRESAR";
    String REGISTRARSE = "REGISTRARSE";

    String getUsuario();
    String getContrasenia();

    void mostrarMensaje(String mensaje);
    void mostrarError(String mensaje);
    void limpiar();

    /** Conecta los botones a quien los escucha. La vista no sabe que es un controlador. */
    void setActionListener(ActionListener listener);

    void mostrar();
    void cerrar();
}