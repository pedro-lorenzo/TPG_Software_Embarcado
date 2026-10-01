package unmdp.fi.programacionc.bitacora;

public final class TipoEvento {
    public static final String SISTEMA  = "SISTEMA";
    public static final String MOTOR    = "MOTOR";
    public static final String RECURSOS = "RECURSOS";
    public static final String MISION   = "MISION";
    public static final String ERROR    = "ERROR";

    private TipoEvento() { }   // no se instancia

    public static boolean esValido(String tipo) {
        return SISTEMA.equals(tipo) || MOTOR.equals(tipo) || RECURSOS.equals(tipo)
                || MISION.equals(tipo) || ERROR.equals(tipo);
    }
}