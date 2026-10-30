package unmdp.fi.programacionc.concurrencia.modelo;

/** Demostracion por consola del universo paralelo, sin interfaz grafica. */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        UniversoParalelo universo = new UniversoParalelo();
        universo.agregarObservador(evento -> System.out.println(evento));

        universo.agregarGeneradora();
        universo.agregarGeneradora();
        universo.agregarNave();
        Thread.sleep(4000);

        universo.agregarNave();                 // se suma en vivo, mientras la simulacion corre
        universo.agregarGeneradora();
        Thread.sleep(4000);

        universo.detener();
        universo.esperarFin();
        System.out.println("Se termino la simulacion");
    }
}