package unmdp.fi.programacionc.mision;

/**
 * Lo que una mision puede consultar y ordenar a su nave. Nada mas.
 * La implementa AsistenteComando (paquete comando).
 *
 * Vive en el paquete mision a proposito: la mision define lo que necesita
 * y es comando quien depende de mision, no al reves.
 */
public interface OperadorMision {

    // ---- consultas ----
    boolean estaDisponible();
    String getEstadoMotor();
    int getCombustible();
    int getEnergia();
    int getDesgaste();

    // ---- ordenes ----
    void consumir(int combustible, int energia, int desgaste);
    void prepararSalto();
    void saltar();
    void finalizarSalto();

    // ---- registro ----
    void registrarEvento(String tipo, String descripcion);
}