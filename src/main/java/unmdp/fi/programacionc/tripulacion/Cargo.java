package unmdp.fi.programacionc.tripulacion;

public final class Cargo {
    public static final String CAPITAN   = "CAPITAN";
    public static final String CONSEJERO = "CONSEJERO";
    public static final String TENIENTE  = "TENIENTE";
    public static final String ALFEREZ   = "ALFEREZ";

    private Cargo() { }

    public static boolean esValido(String cargo) {
        return CAPITAN.equals(cargo) || CONSEJERO.equals(cargo)
                || TENIENTE.equals(cargo) || ALFEREZ.equals(cargo);
    }
}