package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;

/**
 * Componente concreto del Decorator: remuneracion correspondiente al cargo.
 * Es el centro de la cadena; no envuelve a ningun otro componente.
 *
 * inv -> importe > 0
 */
public class HaberBase implements ComponenteHaber {

    private final String concepto;
    private final double importe;

    /**
     * pre -> tripulante != null
     */
    public HaberBase(Tripulante tripulante) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        this.concepto = "Remuneracion por cargo (" + tripulante.getCargo() + ")";
        this.importe = remuneracion(tripulante.getCargo());
    }

    /**
     * Remuneracion mensual del cargo, en PG. Tambien la usa AdicionalAntiguedad,
     * que se calcula sobre este valor y no sobre el total acumulado.
     * pre -> cargo != null
     */
    static double remuneracion(Cargo cargo) {
        switch (cargo) {
            case CAPITAN:   return 1000;
            case CONSEJERO: return 600;
            case TENIENTE:  return 400;
            case ALFEREZ:   return 200;
            default: throw new AssertionError("Cargo no contemplado: " + cargo);
        }
    }

    @Override public double getImporte()          { return importe; }
    @Override public double getAporte()           { return importe; }
    @Override public String getConcepto()         { return concepto; }
    @Override public ComponenteHaber getInterno() { return null; }
}
