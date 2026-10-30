package unmdp.fi.programacionc.concurrencia.modelo;

import java.util.Random;

/**
 * Productor. Es un Runnable: describe la tarea, el hilo se crea en el Main.
 */
public class GeneradoraDeEnergia implements Runnable {
    private final PlataformaDeCarga plataforma;
    private final int cantidadADepositar;
    private final Random random = new Random();

    public GeneradoraDeEnergia(PlataformaDeCarga plataforma, int cantidadADepositar) {
        this.plataforma = plataforma;
        this.cantidadADepositar = cantidadADepositar;
    }

    @Override
    public void run() {
        String nombre = Thread.currentThread().getName();
        try {
            for (int i = 0; i < cantidadADepositar; i++) {
                Thread.sleep(100 + random.nextInt(200));   // tarda en producir la bateria
                plataforma.deposita();
            }
            System.out.println(nombre + " termino su turno");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();   // restauramos la marca de interrupcion
        }
    }
}