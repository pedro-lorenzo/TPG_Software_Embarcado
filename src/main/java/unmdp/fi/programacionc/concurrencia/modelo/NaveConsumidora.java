package unmdp.fi.programacionc.concurrencia.modelo;

import unmdp.fi.programacionc.concurrencia.observer.EmisorEventos;

import java.util.Random;

/** Consumidor: pide una bateria, la retira de la plataforma y se abastece, una y otra vez. */
public class NaveConsumidora extends EntidadParalela {

    private final PlataformaDeCarga plataforma;
    private final Random azar = new Random();

    /**
     * pre -> plataforma != null (el resto lo verifica EntidadParalela)
     */
    public NaveConsumidora(String nombre, PlataformaDeCarga plataforma, EmisorEventos emisor) {
        super(nombre, emisor);
        assert plataforma != null : "Falta la plataforma";
        this.plataforma = plataforma;
    }

    @Override
    protected void ciclo() throws InterruptedException {
        getEmisor().emitir(getNombre() + " solicitando batería.");
        plataforma.retirar(getNombre());
        Thread.sleep(500 + azar.nextInt(500));          // se abastece (fuera del monitor)
    }
}