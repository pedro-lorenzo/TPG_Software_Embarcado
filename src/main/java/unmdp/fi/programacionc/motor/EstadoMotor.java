package unmdp.fi.programacionc.motor;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

/**
 * Estado del motor warp (patron State).
 *
 * Cada estado decide que proceso del motor ejecutar y a que estado pasar; el motor solo sabe
 * hacer los procesos (consumir y avanzar), asi ningun estado necesita un switch/if sobre el nombre.
 * saltarWarp hace por defecto el salto normal: solo Warp lo sobreescribe.
 * Si no alcanza el combustible se lanza OperacionInvalidaException antes de cambiar el estado.
 */
public interface EstadoMotor {

    /**
     * Salto simple (COMBUSTIBLE_SALTO de combustible, avanza 1) y transicion propia del estado.
     * @return casilleros avanzados
     * @throws OperacionInvalidaException si no alcanza el combustible (el estado no cambia)
     */
    int saltar(MotorWarp motor);

    /**
     * Por defecto no es un salto warp: hace lo mismo que saltar.
     * @return casilleros avanzados
     * @throws OperacionInvalidaException si no alcanza el combustible (el estado no cambia)
     */
    default int saltarWarp(MotorWarp motor) { return saltar(motor); }

    default boolean estaDisponible() { return false; }

    String getNombre();
}
