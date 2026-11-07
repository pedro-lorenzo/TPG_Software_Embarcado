package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;

/**
 * Lo que el controlador puede pedirle a la seccion de Mision (iniciar y seguir el tablero en vivo).
 * El tablero llega como una matriz de simbolos: la vista no conoce los elementos del universo.
 */
public interface IVistaMision {

    String INICIAR = "INICIAR_MISION";

    void cargarMisiones(String[] nombres);
    int getMisionElegida();

    /**
     * Dibuja el tablero. celdas[x][y] es el simbolo del elemento en esa casilla (' ' si esta vacia).
     */
    void mostrarTablero(char[][] celdas, int xNave, int yNave);
    void limpiarTablero();

    void mostrarEstado(String texto, boolean alerta);
    void habilitarInicio(boolean habilitado);
    void mostrarError(String mensaje);

    void setActionListener(ActionListener listener);
}
