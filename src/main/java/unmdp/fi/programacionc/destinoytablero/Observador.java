package unmdp.fi.programacionc.destinoytablero;

/** Recibe avisos cuando se actualiza el tablero observado. */
public interface Observador {
    /** Ejecuta la actualización ante un cambio en el tablero. */
    void actualizar();
}
