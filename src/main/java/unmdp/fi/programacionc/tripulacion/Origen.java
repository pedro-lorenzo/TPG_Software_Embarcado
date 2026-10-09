package unmdp.fi.programacionc.tripulacion;

public final class Origen {
    public static final String TERRICOLA = "TERRICOLA";
    public static final String VULCANO   = "VULCANO";
    public static final String MARCIANO  = "MARCIANO";

    private Origen() { }

    public static boolean esValido(String origen) {
        return TERRICOLA.equals(origen) || VULCANO.equals(origen) || MARCIANO.equals(origen);
    }
}