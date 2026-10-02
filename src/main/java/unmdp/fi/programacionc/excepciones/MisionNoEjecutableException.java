package unmdp.fi.programacionc.excepciones;

public class MisionNoEjecutableException extends RuntimeException {
    public MisionNoEjecutableException(String motivo) {
        super(motivo);
    }
}
