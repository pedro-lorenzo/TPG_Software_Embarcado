package unmdp.fi.programacionc.concurrencia.modelo;

public class PlataformaDeCarga {
    private final int capacidad;
    private int baterias = 0;

    public PlataformaDeCarga(int capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Lo llaman las generadoras. Si la plataforma esta llena, la generadora espera.
     */
    public synchronized void deposita() throws InterruptedException {
        System.out.println("Bateria producida por " + nombreHilo() + ".");
        while (baterias == capacidad) {
            System.out.println(nombreHilo() + " esperando a que se libere espacio en la Plataforma.");
            wait();
        }

        baterias++;
        System.out.println(nombreHilo() + " deposito una bateria (plataforma: " + baterias + "/" + capacidad + ").");
        notifyAll();
    }

    /**
     * Lo llaman las naves. Si la plataforma esta vacia, la nave espera.
     */
    public synchronized void retira() throws InterruptedException {
        System.out.println(nombreHilo() + " solicitando bateria.");
        while (baterias == 0) {
            System.out.println(nombreHilo() + " esperando por falta de baterias en la Plataforma.");
            wait();
        }

        baterias--;
        System.out.println("Bateria retirada y utilizada por " + nombreHilo() + " (plataforma: " + baterias + "/" + capacidad + ").");
        notifyAll();
    }

    private String nombreHilo() {
        return Thread.currentThread().getName();
    }
}