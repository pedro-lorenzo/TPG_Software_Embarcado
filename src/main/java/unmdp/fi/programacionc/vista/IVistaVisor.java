package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;

/**
 * Lo que el controlador puede pedirle al visor del universo paralelo.
 * Todos estos metodos se llaman desde el hilo de Swing (el controlador se encarga).
 */
public interface IVistaVisor {

    String CREAR_GENERADORA = "CREAR_GENERADORA";
    String CREAR_NAVE = "CREAR_NAVE";

    void mostrarContadores(int generadoras, int naves);

    /** Agrega una linea al final de la consola de eventos. */
    void agregarEvento(String linea);

    void setActionListener(ActionListener listener);
}
