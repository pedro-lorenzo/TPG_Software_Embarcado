package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.tablero.Coordenada;

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
    /**
     * pre -> la nave ya tiene posicion (se la asigna la primera llamada a moverA)
     */
    Coordenada getPosicion();

    /** Una nave destruida no se reusa: la mision tiene que terminar. */
    boolean estaDestruida();

    int getGemas();

    // ---- ordenes (lanzan OperacionInvalidaException si la nave las rechaza) ----
    void consumir(int combustible, int energia, int desgaste);
    void prepararSalto();
    void saltar();
    void finalizarSalto();
    /**
     * Ubica a la nave en esa coordenada (la primera vez la ubica, despues la mueve).
     * pre -> destino != null
     * pre -> la nave no esta destruida
     */
    void moverA(Coordenada destino);

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


    // ---- registro en la bitacora del asistente ----
    void registrarEvento(TipoEvento tipo, String descripcion);
}
