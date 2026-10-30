package unmdp.fi.programacionc.concurrencia.observer;

/**
 * Lo que necesitan la plataforma, las generadoras y las naves para informar un evento,
 * sin conocer a quien lo recibe.
 */
public interface EmisorEventos {
    /**
     * pre -> mensaje != null
     */
    void emitir(String mensaje);
}