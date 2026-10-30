package unmdp.fi.programacionc.concurrencia.observer;

/**
 * Quien quiera enterarse de lo que pasa en el universo paralelo (consola, panel de texto de la GUI).
 *
 * Contrato: actualizar debe volver rapido y no puede llamar de vuelta al universo paralelo.
 * Se invoca desde los hilos de las generadoras y las naves, no desde el hilo de Swing:
 * una vista Swing tiene que encolar el cambio con SwingUtilities.invokeLater.
 */
public interface ObservadorVisor {
    void actualizar(String evento);
}