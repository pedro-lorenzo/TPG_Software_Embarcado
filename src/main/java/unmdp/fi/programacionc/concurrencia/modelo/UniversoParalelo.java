package unmdp.fi.programacionc.concurrencia.modelo;

import unmdp.fi.programacionc.concurrencia.observer.EmisorEventos;
import unmdp.fi.programacionc.concurrencia.observer.ObservadorVisor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Universo paralelo: una plataforma y las generadoras y naves que se agregan en vivo.
 * Es el Observable: avisa cada evento a sus observadores (la GUI llama a agregarGeneradora y
 * agregarNave desde los botones y se suscribe con agregarObservador).
 *
 * La lista de observadores es CopyOnWriteArrayList porque se recorre desde varios hilos
 * mientras otro puede suscribirse.
 */
public final class UniversoParalelo implements EmisorEventos {

    public static final int CAPACIDAD_PLATAFORMA = 3;

    private final PlataformaDeCarga plataforma;
    private final List<ObservadorVisor> observadores = new CopyOnWriteArrayList<>();
    private final List<EntidadParalela> entidades = new ArrayList<>();
    private final List<Thread> hilos = new ArrayList<>();
    private int generadoras = 0;
    private int naves = 0;

    public UniversoParalelo() {
        this.plataforma = new PlataformaDeCarga(CAPACIDAD_PLATAFORMA, this);
    }

    // ---------- Observable ----------
    public void agregarObservador(ObservadorVisor o) {
        assert o != null : "El observador no puede ser nulo";
        observadores.add(o);
    }

    public void quitarObservador(ObservadorVisor o) {
        observadores.remove(o);
    }

    @Override
    public void emitir(String mensaje) {
        assert mensaje != null : "El mensaje no puede ser nulo";
        for (ObservadorVisor o : observadores) {
            o.actualizar(mensaje);
        }
    }

    // ---------- Operaciones (las llaman los botones) ----------
    public synchronized void agregarGeneradora() {
        generadoras++;
        String nombre = "Generadora " + generadoras;
        emitir("¡Ingresó una nueva Generadora al universo paralelo! (" + nombre + ")");
        iniciar(new GeneradoraDeEnergia(nombre, plataforma, this));
    }

    public synchronized void agregarNave() {
        naves++;
        String nombre = "Nave " + naves;
        emitir("¡Ingresó una nueva Nave al universo paralelo! (" + nombre + ")");
        iniciar(new NaveConsumidora(nombre, plataforma, this));
    }

    /** Detiene todos los hilos. Los que esperan en wait() o sleep() despiertan por la interrupcion. */
    public synchronized void detener() {
        for (EntidadParalela e : entidades) {
            e.detener();
        }
        for (Thread t : hilos) {
            t.interrupt();
        }
    }

    /** Espera a que terminen todos los hilos (para demostraciones por consola). */
    public void esperarFin() throws InterruptedException {
        List<Thread> copia;
        synchronized (this) {
            copia = new ArrayList<>(hilos);
        }
        for (Thread t : copia) {
            t.join();
        }
    }

    private void iniciar(EntidadParalela entidad) {
        Thread hilo = new Thread(entidad, entidad.getNombre());
        entidades.add(entidad);
        hilos.add(hilo);
        hilo.start();
    }
}