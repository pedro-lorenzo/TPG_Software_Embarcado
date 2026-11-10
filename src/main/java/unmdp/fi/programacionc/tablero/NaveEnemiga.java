// NaveEnemiga.java
package unmdp.fi.programacionc.tablero;

/**
 * Nave que se cruza con la nave propia. Double Dispatch:
 *   1) nave.enfrentar(this)     -> decide la clase de la nave propia
 *   2) enemiga.recibirDeXxx()   -> decide la clase de la enemiga
 * Sin if ni switch sobre el tipo. El resultado es desde el punto de vista de la nave propia.
 */
public abstract class NaveEnemiga implements ElementoDelUniverso {

    private final String nombre;

    /**
     * pre -> nombre no nulo ni vacio
     */
    protected NaveEnemiga(String nombre) {
        assert nombre != null && !nombre.trim().isEmpty() : "El nombre no puede ser nulo ni vacio";
        this.nombre = nombre;
    }

    public abstract ResultadoBatalla recibirDeExplorador();
    public abstract ResultadoBatalla recibirDeCarguero();
    public abstract ResultadoBatalla recibirDeCombate();

    /**
     * pre -> nave != null
     * post -> si la nave propia pierde, queda destruida
     */
    @Override public String interactuar(NavePropia nave) {
        assert nave != null : "La nave no puede ser nula";
        ResultadoBatalla resultado = nave.enfrentar(this);
        if (resultado == ResultadoBatalla.PIERDE) {
            nave.destruir("derrotada por " + nombre);
        }
        return "Encuentro con " + nombre + ": " + resultado;
    }

    @Override public boolean esObstaculo() { return false; }

    public String getNombre() { return nombre; }
}