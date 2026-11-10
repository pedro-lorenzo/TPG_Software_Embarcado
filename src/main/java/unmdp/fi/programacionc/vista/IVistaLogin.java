package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;

/**
 * Lo que el controlador puede pedirle a la pantalla de acceso.
 * El controlador conoce esta interfaz, nunca la ventana concreta.
 */
public interface IVistaLogin {

    // Comandos de los botones: el controlador los usa para saber cual se apreto
    String INGRESAR = "INGRESAR";
    String IR_A_REGISTRO = "IR_A_REGISTRO";

    String getUsuario();
    String getContrasenia();

    void mostrarMensaje(String mensaje);
    void mostrarError(String mensaje);
    void limpiar();

    /** Conecta los botones a quien los escucha. La vista no sabe que es un controlador. */
    void setActionListener(ActionListener listener);

    /** Deja escrito un usuario (por ejemplo, el que se acaba de registrar). */
    void setUsuario(String usuario);

    void mostrar();
    /** La esconde sin destruirla, para volver a mostrarla despues (al salir del registro). */
    void ocultar();
    void cerrar();
}