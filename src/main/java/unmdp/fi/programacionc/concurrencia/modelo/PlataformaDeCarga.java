package unmdp.fi.programacionc.concurrencia.modelo;

import unmdp.fi.programacionc.concurrencia.observer.EmisorEventos;

/**
 * Recurso compartido (monitor). Capacidad limitada y acceso de una sola entidad a la vez.
 * Productor-consumidor: las generadoras depositan y esperan si esta llena;
 * las naves retiran y esperan si esta vacia.
 *
 * Los eventos se emiten dentro del monitor para que su orden coincida con el orden real
 * de los cambios de estado; por eso el emisor tiene que volver rapido (ver ObservadorVisor).
 *
 * inv -> 0 <= baterias <= capacidad
 */
public class PlataformaDeCarga {

    private final int capacidad;
    private final EmisorEventos emisor;
    private int baterias = 0;

    /**
     * pre -> capacidad > 0
     * pre -> emisor != null
     */
    public PlataformaDeCarga(int capacidad, EmisorEventos emisor) {
        assert capacidad > 0 : "La capacidad debe ser positiva";
        assert emisor != null : "Falta el emisor de eventos";
        this.capacidad = capacidad;
        this.emisor = emisor;
    }

    /**
     * Lo llaman las generadoras. Si la plataforma esta llena, espera hasta que haya lugar.
     * post -> la plataforma tiene una bateria mas
     */
    public synchronized void depositar(String generadora) throws InterruptedException {
        boolean avisoEspera = false;
        while (baterias == capacidad) {
            if (!avisoEspera) {
                emisor.emitir(generadora + " esperando a que se libere espacio en la Plataforma.");
                avisoEspera = true;
            }
            wait();
        }
        baterias++;
        emisor.emitir("Batería depositada por " + generadora + " (plataforma: " + baterias + "/" + capacidad + ").");
        notifyAll();
    }

    /**
     * Lo llaman las naves. Si la plataforma esta vacia, espera hasta que haya una bateria.
     * post -> la plataforma tiene una bateria menos
     */
    public synchronized void retirar(String nave) throws InterruptedException {
        boolean avisoEspera = false;
        while (baterias == 0) {
            if (!avisoEspera) {
                emisor.emitir(nave + " esperando por falta de baterías en la Plataforma.");
                avisoEspera = true;
            }
            wait();
        }
        baterias--;
        emisor.emitir("Batería retirada y utilizada por " + nave + " (plataforma: " + baterias + "/" + capacidad + ").");
        notifyAll();
    }
}