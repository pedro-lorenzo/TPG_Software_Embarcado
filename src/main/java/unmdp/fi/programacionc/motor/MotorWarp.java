package unmdp.fi.programacionc.motor;

import unmdp.fi.programacionc.recursos.Tanque;

/**
 * Motor warp: contexto del patron State (Aclaracion R3).
 *
 * Su comportamiento publico es saltar() y saltarWarp(): ambas delegan en el estado actual, que
 * decide que proceso ejecutar y a que estado pasar (setEstado es package-private). Los procesos
 * (consumir recursos del tanque y avanzar) son metodos package-private que llaman los estados.
 *
 * Estados y transiciones:
 *   Disponible       --saltar-->      Preparando salto
 *   Preparando salto --saltar-->      Warp (si energia >= ENERGIA_WARP; si no, se queda)
 *   Warp             --saltar-->      Warp
 *   Warp             --saltarWarp-->  Enfriamiento (avanza 2) o, sin energia, Preparando salto (salto simple)
 *   Enfriamiento     --saltar-->      Disponible (suma DESGASTE_ENFRIAMIENTO de desgaste)
 * saltarWarp en cualquier otro estado equivale a saltar. Todo salto simple cuesta COMBUSTIBLE_SALTO
 * y avanza 1; si no alcanza el combustible se lanza OperacionInvalidaException y el estado no cambia.
 *
 * inv -> estado != null y tanque != null
 * El motor arranca en Disponible.
 */
public class MotorWarp {

    public static final int COMBUSTIBLE_SALTO = 2;
    public static final int ENERGIA_WARP = 5;
    public static final int DESGASTE_ENFRIAMIENTO = 3;

    private static final int CASILLEROS_SALTO = 1;
    private static final int CASILLEROS_SALTO_WARP = 2;

    private final Tanque tanque;
    private EstadoMotor estado = new Disponible();

    /**
     * pre -> tanque != null (el de la nave que mueve este motor)
     */
    public MotorWarp(Tanque tanque) {
        assert tanque != null : "El motor necesita el tanque de la nave";
        this.tanque = tanque;
    }

    /**
     * @return casilleros avanzados
     * @throws unmdp.fi.programacionc.excepciones.OperacionInvalidaException si no alcanza el combustible
     */
    public int saltar()     { return estado.saltar(this); }

    /**
     * @return casilleros avanzados (2 si fue salto warp, 1 si fue salto simple)
     * @throws unmdp.fi.programacionc.excepciones.OperacionInvalidaException si no alcanza el combustible
     */
    public int saltarWarp() { return estado.saltarWarp(this); }

    public boolean estaDisponible() { return estado.estaDisponible(); }
    public String getEstadoActual() { return estado.getNombre(); }

    // ---- procesos que usan los estados (consumen del tanque; si el tanque rechaza, no cambia nada) ----

    int avanzarUnCasillero() {
        tanque.consumir(COMBUSTIBLE_SALTO, 0, 0);
        return CASILLEROS_SALTO;
    }

    /** Salto simple y desgaste de enfriamiento en una sola operacion del tanque (atomica). */
    int avanzarUnCasilleroCobrandoEnfriamiento() {
        tanque.consumir(COMBUSTIBLE_SALTO, 0, DESGASTE_ENFRIAMIENTO);
        return CASILLEROS_SALTO;
    }

    boolean hayEnergiaParaWarp() {
        return tanque.getEnergia() >= ENERGIA_WARP;
    }

    /** pre -> hayEnergiaParaWarp() */
    int ejecutarSaltoWarp() {
        assert hayEnergiaParaWarp() : "No hay energia para el salto warp";
        tanque.consumir(0, ENERGIA_WARP, 0);
        return CASILLEROS_SALTO_WARP;
    }

    /**
     * Solo lo usan los estados (mismo paquete) para concretar una transicion.
     * pre -> nuevo != null
     */
    void setEstado(EstadoMotor nuevo) {
        assert nuevo != null : "El motor siempre tiene un estado";
        this.estado = nuevo;
    }
}
