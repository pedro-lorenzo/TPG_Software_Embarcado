package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.tablero.NavePropia;;

/**
 * Lo que una mision puede consultar y ordenar a la nave, a traves de su asistente. Nada mas.
 * La extiende Asistente y la implementa AsistenteComando (paquete comando).
 *
 * Vive en el paquete mision a proposito: la mision define lo que necesita y es el paquete
 * comando el que depende de mision, no al reves (Inversion de Dependencias). Solo expone
 * lo que una mision usa (Segregacion de Interfaces).
 */
public interface OperadorMision extends NavePropia{

    // ---- consultas ----
    boolean estaDisponible();
    String getEstadoMotor();
    int getCombustible();
    int getEnergia();
    int getDesgaste();
    boolean necesitaMantenimiento();

    /** Una nave destruida no se reusa: la mision tiene que terminar. */
    boolean estaDestruida();

    int getGemas();

    // ---- ordenes (lanzan OperacionInvalidaException si la nave las rechaza) ----
    void consumir(int combustible, int energia, int desgaste);
    /**
     * Salto simple del motor.
     * @return casilleros avanzados
     * @throws unmdp.fi.programacionc.excepciones.OperacionInvalidaException si no alcanza el combustible
     */
    int saltar();
    /**
     * Salto warp si el motor esta listo; en cualquier otro estado hace el salto simple.
     * @return casilleros avanzados
     * @throws unmdp.fi.programacionc.excepciones.OperacionInvalidaException si no alcanza el combustible
     */
    int saltarWarp();
    /**
     * pre -> cantidad > 0
     */
    void agregarGemas(int cantidad);

    /**
     * Convierte todas las gemas en combustible (COMBUSTIBLE_POR_GEMA cada una).
     * Lo que no entra en el tanque se pierde.
     * post -> getGemas() == 0
     */
    void canjearGemas();

    /**
     * Lleva combustible y energia al tope.
     * post -> combustible == Tanque.TOPE_COMBUSTIBLE y energia == Tanque.TOPE_ENERGIA
     */
    void recargarTotal();

    /**
     * post -> getDesgaste() == 0
     */
    void realizarMantenimiento();


    // ---- registro en la bitacora del asistente ----
    void registrarEvento(TipoEvento tipo, String descripcion);
    void registrarInforme(InformeMision informe);
}
