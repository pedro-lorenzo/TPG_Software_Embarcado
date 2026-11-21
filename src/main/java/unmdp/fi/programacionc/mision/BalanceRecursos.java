package unmdp.fi.programacionc.mision;

/**
 * Balance de recursos de una mision, separado por causa (Fase 3: "balance detallado de los
 * recursos ganados, consumidos o perdidos durante el trayecto").
 *
 *  - Consumido: lo que gasta la propia nave (despegue, saltos, salto warp y enfriamiento).
 *  - Ganado:    lo que recibe de los elementos del universo (estaciones de carga).
 *  - Perdido:   lo que le quitan los elementos del universo (asteroides).
 *
 * Se usa en dos pasos: anotarAntes() guarda los recursos actuales y, despues de la accion,
 * registrarConsumo() o registrarEncuentro() suma la diferencia en la categoria que corresponde.
 * Asi una estacion de carga no "esconde" lo que se gasto en saltos, como pasaba con el neto.
 *
 * inv -> todos los acumulados son >= 0
 */
public class BalanceRecursos {

    // Recursos anotados por anotarAntes()
    private int combustibleAntes;
    private int energiaAntes;
    private int desgasteAntes;

    private int combustibleConsumido;
    private int energiaConsumida;
    private int desgasteProducido;
    private int combustibleGanado;
    private int energiaGanada;
    private int combustiblePerdido;
    private int energiaPerdida;

    /**
     * Anota los recursos actuales de la nave, antes de una accion.
     * pre -> nave != null
     */
    public void anotarAntes(OperadorMision nave) {
        assert nave != null : "La nave no puede ser nula";
        combustibleAntes = nave.getCombustible();
        energiaAntes = nave.getEnergia();
        desgasteAntes = nave.getDesgaste();
    }

    /**
     * Suma como consumo lo que gasto la nave desde anotarAntes() (despegue o salto).
     * pre -> se llamo a anotarAntes() antes de la accion
     * pre -> la accion solo pudo bajar combustible y energia y subir el desgaste
     */
    public void registrarConsumo(OperadorMision nave) {
        assert nave != null : "La nave no puede ser nula";
        combustibleConsumido += combustibleAntes - nave.getCombustible();
        energiaConsumida += energiaAntes - nave.getEnergia();
        desgasteProducido += nave.getDesgaste() - desgasteAntes;
        assert verificarInvariante() : "Un consumo no puede aumentar los recursos";
    }

    /**
     * Suma lo que cambio por un elemento del universo desde anotarAntes():
     * si un recurso subio es ganancia, si bajo es perdida.
     * pre -> se llamo a anotarAntes() antes de interactuar con el elemento
     */
    public void registrarEncuentro(OperadorMision nave) {
        assert nave != null : "La nave no puede ser nula";
        int diferenciaCombustible = nave.getCombustible() - combustibleAntes;
        int diferenciaEnergia = nave.getEnergia() - energiaAntes;
        if (diferenciaCombustible > 0) {
            combustibleGanado += diferenciaCombustible;
        } else {
            combustiblePerdido -= diferenciaCombustible;
        }
        if (diferenciaEnergia > 0) {
            energiaGanada += diferenciaEnergia;
        } else {
            energiaPerdida -= diferenciaEnergia;
        }
        desgasteProducido += Math.max(0, nave.getDesgaste() - desgasteAntes);
        assert verificarInvariante();
    }

    private boolean verificarInvariante() {
        return combustibleConsumido >= 0 && energiaConsumida >= 0 && desgasteProducido >= 0
                && combustibleGanado >= 0 && energiaGanada >= 0
                && combustiblePerdido >= 0 && energiaPerdida >= 0;
    }

    public int getCombustibleConsumido() { return combustibleConsumido; }
    public int getEnergiaConsumida()     { return energiaConsumida; }
    public int getDesgasteProducido()    { return desgasteProducido; }
    public int getCombustibleGanado()    { return combustibleGanado; }
    public int getEnergiaGanada()        { return energiaGanada; }
    public int getCombustiblePerdido()   { return combustiblePerdido; }
    public int getEnergiaPerdida()       { return energiaPerdida; }
}
