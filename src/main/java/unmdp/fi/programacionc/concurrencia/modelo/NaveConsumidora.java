package unmdp.fi.programacionc.concurrencia.modelo;
import java.util.Random;

/**
 * Consumidor. Tambien es un Runnable.
 */
public class NaveConsumidora implements Runnable {
    private final PlataformaDeCarga plataforma;
    private final int cantidadARetirar;
    private final Random random = new Random();

    public NaveConsumidora(PlataformaDeCarga plataforma, int cantidadARetirar) {
        this.plataforma = plataforma;
        this.cantidadARetirar = cantidadARetirar;
    }

    @Override
    public void run() {
        String nombre = Thread.currentThread().getName();
        try {
            for (int i = 0; i < cantidadARetirar; i++) {
                plataforma.retira();
                Thread.sleep(300 + random.nextInt(300));   // se abastece con la bateria
            }
            System.out.println(nombre + " no necesita mas baterias");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}