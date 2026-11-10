package unmdp.fi.programacionc.tablero;

/** Todo lo que puede haber en una casilla del tablero ademas de la nave propia. */
public interface ElementoDelUniverso {
    /**
     * pre -> nave != null
     * @return descripcion de lo que paso, para que la mision la registre en la bitacora
     */
    String interactuar(NavePropia nave);

    /** Caracter para dibujar el elemento en el tablero. */
    char getSimbolo();

    /** Indica si el radar lo detecta como un obstaculo que conviene esquivar con el salto Warp. */
    boolean esObstaculo();
}