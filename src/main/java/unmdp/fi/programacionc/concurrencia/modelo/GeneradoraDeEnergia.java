package unmdp.fi.programacionc.concurrencia.modelo;

import unmdp.fi.programacionc.concurrencia.observer.EmisorEventos;

import java.util.Random;

/** Productor: produce una bateria y la deposita en la plataforma, una y otra vez. */
public class GeneradoraDeEnergia extends EntidadParalela {

    private final PlataformaDeCarga plataforma;
    private final Random azar = new Random();

    /**
     * pre -> plataforma != null (el resto lo verifica EntidadParalela)
     */
    public GeneradoraDeEnergia(String nombre, PlataformaDeCarga plataforma, EmisorEventos emisor) {
        super(nombre, emisor);
        assert plataforma != null : "Falta la plataforma";
        this.plataforma = plataforma;
    }

    @Override
    protected void ciclo() throws InterruptedException {
        Thread.sleep(300 + azar.nextInt(400));          // tarda en producir (fuera del monitor)
        getEmisor().emitir("Batería producida por " + getNombre() + ".");
        plataforma.depositar(getNombre());
    }
}