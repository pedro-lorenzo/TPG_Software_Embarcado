package unmdp.fi.programacionc;

import unmdp.fi.programacionc.controlador.ControladorLogin;
import unmdp.fi.programacionc.persistencia.ControlAcceso;
import unmdp.fi.programacionc.persistencia.UsuarioDAOJdbc;
import unmdp.fi.programacionc.vista.Tema;
import unmdp.fi.programacionc.vista.VentanaLogin;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la interfaz grafica. La demo por consola sigue en Main.
 * Arma las piezas y las conecta: modelo, vista y controlador.
 */
public class AppGrafica {

    public static void main(String[] args) {
        // Swing se arma siempre en su propio hilo (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            Tema.aplicarGlobal();
            ControlAcceso controlAcceso = new ControlAcceso(new UsuarioDAOJdbc());
            VentanaLogin vista = new VentanaLogin();
            new ControladorLogin(vista, controlAcceso);
            vista.mostrar();
        });
    }
}