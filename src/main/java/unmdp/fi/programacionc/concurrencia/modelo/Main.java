package unmdp.fi.programacionc.concurrencia.modelo;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PlataformaDeCarga plataforma = new PlataformaDeCarga(3);   // UNA sola plataforma compartida

        List<Thread> hilos = new ArrayList<>();
        hilos.add(new Thread(new GeneradoraDeEnergia(plataforma, 4), "Generadora 1"));
        hilos.add(new Thread(new GeneradoraDeEnergia(plataforma, 4), "Generadora 2"));
        hilos.add(new Thread(new GeneradoraDeEnergia(plataforma, 4), "Generadora 3"));
        hilos.add(new Thread(new NaveConsumidora(plataforma, 6), "Nave 1"));
        hilos.add(new Thread(new NaveConsumidora(plataforma, 6), "Nave 2"));

        for (Thread hilo : hilos) {
            hilo.start();
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }
        System.out.println("Se termino la simulacion");
    }
}