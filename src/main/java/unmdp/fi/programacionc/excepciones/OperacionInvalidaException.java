package unmdp.fi.programacionc.excepciones;

/**
 * Una orden no pudo cumplirse por el estado actual del sistema: transicion invalida del motor,
 * carga que excede la capacidad, recursos insuficientes, universo lleno o nave inexistente.
 * Quien la lanza no modifica nada antes de lanzarla.
 */
public class OperacionInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
