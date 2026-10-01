package unmdp.fi.programacionc.excepciones;

// Se lanza solo ante condiciones que dependen del estado actual de un objeto y que el
// llamador no puede garantizar de antemano (transicion invalida del motor, capacidad
// excedida, recursos insuficientes). Se propaga hasta el Asistente de Comando, que la
// registra en la Bitacora. Los argumentos invalidos NO usan esta excepcion: son
// precondiciones que se verifican con assert.
public class OperacionInvalidaException extends RuntimeException {

    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
