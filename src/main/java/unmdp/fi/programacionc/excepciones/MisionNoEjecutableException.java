package unmdp.fi.programacionc.excepciones;

/**
 * La preparacion de una mision detecto que no puede realizarse (motor no disponible,
 * mantenimiento pendiente o recursos insuficientes). La lanza y la captura Mision,
 * que cierra la mision como RECHAZADA sin modificar la nave.
 */
public class MisionNoEjecutableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MisionNoEjecutableException(String motivo) {
        super(motivo);
    }
}
