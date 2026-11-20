package unmdp.fi.programacionc.destinoytablero;
/** Recibe avisos cuando se actualiza el tablero observado. */
public interface Observador {
    /**
     * Ejecuta la actualización ante un cambio en el tablero, recibiendo un
     * mensaje que describe lo ocurrido.
     * @param mensaje texto enviado por el objeto observado
     */
    void actualizar(String mensaje);
}