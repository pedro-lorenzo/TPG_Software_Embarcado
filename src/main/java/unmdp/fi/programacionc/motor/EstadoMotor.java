package unmdp.fi.programacionc.motor;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

/**
 * Estado del motor warp (patron State).
 *
 * Cada estado concreto sobreescribe solo las acciones validas en el; las demas quedan
 * rechazadas por los metodos default de esta interfaz, asi ningun estado necesita un switch/if.
 * Se rechaza con excepcion (y no con assert) porque la validez depende del estado actual,
 * que quien da la orden no controla. Un rechazo no modifica el estado (Aclaracion R3).
 */
public interface EstadoMotor {

    default void prepararSalto(MotorWarp motor)         { rechazar("prepararSalto"); }
    default void saltar(MotorWarp motor)                { rechazar("saltar"); }
    default void enfriar(MotorWarp motor)               { rechazar("enfriar"); }
    default void finalizarSalto(MotorWarp motor)        { rechazar("finalizarSalto"); }
    default void completarEnfriamiento(MotorWarp motor) { rechazar("completarEnfriamiento"); }

    default boolean estaDisponible() { return false; }

    String getNombre();

    /**
     * @throws OperacionInvalidaException siempre: la accion no es valida en este estado
     */
    default void rechazar(String accion) {
        throw new OperacionInvalidaException(
                "Transicion invalida: " + accion + " no es valida en el estado " + getNombre());
    }
}
