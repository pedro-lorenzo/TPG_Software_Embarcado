package unmdp.fi.programacionc.nave;

/**
 * Tipos de nave admitidos (E1-01). Constantes en lugar de enum, igual que TipoEvento.
 * esValido permite que la fabrica y la nave rechacen un tipo inexistente.
 */
public final class TipoNave {
    public static final String EXPLORADORA = "EXPLORADORA";
    public static final String CARGUERO    = "CARGUERO";
    public static final String COMBATE     = "COMBATE";

    private TipoNave() { }   // no se instancia

    public static boolean esValido(String tipo) {
        return EXPLORADORA.equals(tipo) || CARGUERO.equals(tipo) || COMBATE.equals(tipo);
    }
}