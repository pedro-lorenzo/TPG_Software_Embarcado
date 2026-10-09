package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;

public class HaberBase implements ComponenteHaber {

    private final String concepto;
    private final double importe;

    /** pre -> tripulante != null */
    public HaberBase(Tripulante tripulante) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        this.concepto = "Remuneracion por cargo (" + tripulante.getCargo() + ")";
        this.importe = remuneracion(tripulante.getCargo());
    }

    // Package-private: tambien la usa AdicionalAntiguedad (se calcula sobre la remuneracion del cargo)
    static double remuneracion(String cargo) {
        switch (cargo) {
            case Cargo.CAPITAN:   return 1000;
            case Cargo.CONSEJERO: return 600;
            case Cargo.TENIENTE:  return 400;
            case Cargo.ALFEREZ:   return 200;
            default: throw new AssertionError("Cargo no contemplado: " + cargo);
        }
    }

    @Override public double getImporte()          { return importe; }
    @Override public double getAporte()           { return importe; }
    @Override public String getConcepto()         { return concepto; }
    @Override public ComponenteHaber getInterno() { return null; }
}