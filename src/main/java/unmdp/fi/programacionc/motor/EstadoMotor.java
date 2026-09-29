package unmdp.fi.programacionc.motor;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

// Patron State: cada estado concreto sobreescribe solo las acciones que le son
// validas. Las acciones no sobreescritas quedan rechazadas por el default de aca,
// asi ninguna clase de estado necesita un switch/if para saber que hacer.
// Se usa excepcion (y no assert) porque la validez depende del estado actual del motor,
// que el llamador no controla, y el enunciado exige que el rechazo quede registrado.
public interface EstadoMotor {

    default void iniciarPreparacion(MotorWarp motor) {
        rechazar("iniciarPreparacion");
    }

    default void confirmarSalto(MotorWarp motor) {
        rechazar("confirmarSalto");
    }

    default void finalizarSalto(MotorWarp motor) {
        rechazar("finalizarSalto");
    }

    default void abortarSalto(MotorWarp motor) {
        rechazar("abortarSalto");
    }

    default void completarEnfriamiento(MotorWarp motor) {
        rechazar("completarEnfriamiento");
    }

    String getNombre();

    default void rechazar(String accion) {
        throw new OperacionInvalidaException(
                "Transicion invalida: " + accion + " no es valida en el estado " + getNombre());
    }
}
