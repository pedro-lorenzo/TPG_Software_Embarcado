package unmdp.fi.programacionc.concurrencia.modelo;

import unmdp.fi.programacionc.concurrencia.observer.EmisorEventos;

/**
 * Entidad que corre en su propio hilo y repite su ciclo hasta que se la detiene.
 * Template Method: run() fija el bucle y el manejo de la detencion; cada entidad define su ciclo.
 *
 * detener() baja el flag y el universo interrumpe el hilo: hace falta porque un hilo dentro
 * de wait() o sleep() no mira el flag hasta despertar.
 */
public abstract class EntidadParalela implements Runnable {

    private final String nombre;
    private final EmisorEventos emisor;
    private volatile boolean activa = true;

    /**
     * pre -> nombre no nulo ni vacio
     * pre -> emisor != null
     */
    protected EntidadParalela(String nombre, EmisorEventos emisor) {
        assert nombre != null && !nombre.trim().isEmpty() : "El nombre no puede ser nulo ni vacio";
        assert emisor != null : "Falta el emisor de eventos";
        this.nombre = nombre;
        this.emisor = emisor;
    }

    /** Una vuelta del ciclo de esta entidad. */
    protected abstract void ciclo() throws InterruptedException;

    @Override
    public final void run() {
        try {
            while (activa) {
                ciclo();
            }
        } catch (InterruptedException e) {
            // pedido de detencion: salimos del bucle
        }
        emisor.emitir(nombre + " se detuvo.");
    }

    public void detener() { activa = false; }

    public String getNombre() { return nombre; }

    protected EmisorEventos getEmisor() { return emisor; }
}