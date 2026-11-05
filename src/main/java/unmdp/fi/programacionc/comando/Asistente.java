package unmdp.fi.programacionc.comando;

import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.mision.OperadorMision;
import unmdp.fi.programacionc.tablero.NavePropia;

import java.util.List;

/**
 * Asistente de comando que opera una nave (Aclaracion R2): todo lo que se le puede consultar
 * u ordenar a una nave. Incluye lo que ven las misiones (OperadorMision) y suma las ordenes
 * que solo da quien opera la nave (cargas y mantenimiento) y la consulta de la bitacora.
 *
 * Tambien es la NavePropia que ven los elementos del tablero (asteroides, estaciones, naves enemigas):
 * el tablero le pasa a cada elemento nave.getAsistente().
 *
 * Nave, sus subclases y Main dependen de esta interfaz y no de una clase concreta: una nueva
 * variante de asistente se agrega implementandola, y solo NaveFactory decide cual crear
 * (Inversion de Dependencias; pedido de diseno de la aclaracion).
 */
public interface Asistente extends OperadorMision, NavePropia {

    /** Eventos de la bitacora en orden temporal. La lista no se puede modificar. */
    List<EventoBitacora> getEventos();

    // ---- ordenes (lanzan OperacionInvalidaException si la nave las rechaza) ----
    void cargarCombustible(int carga);
    void cargarEnergia(int carga);
    void realizarMantenimiento();
}