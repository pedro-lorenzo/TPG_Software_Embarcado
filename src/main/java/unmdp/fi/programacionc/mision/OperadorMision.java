package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;

/**
 * Lo que una mision puede consultar y ordenar a la nave, a traves de su asistente. Nada mas.
 * La extiende Asistente y la implementa AsistenteComando (paquete comando).
 *
 * Vive en el paquete mision a proposito: la mision define lo que necesita y es el paquete
 * comando el que depende de mision, no al reves (Inversion de Dependencias). Solo expone
 * lo que una mision usa (Segregacion de Interfaces).
 */
public interface OperadorMision {

    // ---- consultas ----
    boolean estaDisponible();
    String getEstadoMotor();
    int getCombustible();
    int getEnergia();
    int getDesgaste();
    boolean necesitaMantenimiento();

    // ---- ordenes (lanzan OperacionInvalidaException si la nave las rechaza) ----
    void consumir(int combustible, int energia, int desgaste);
    void prepararSalto();
    void saltar();
    void finalizarSalto();

    // ---- registro en la bitacora del asistente ----
    void registrarEvento(TipoEvento tipo, String descripcion);
}
