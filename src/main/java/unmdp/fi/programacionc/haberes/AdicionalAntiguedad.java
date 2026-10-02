package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;

public class AdicionalAntiguedad extends DecoratorHaber {

    /** pre -> componente != null y tripulante != null */
    public AdicionalAntiguedad(ComponenteHaber componente, Tripulante tripulante) {
        super(componente,
                "Adicional por antiguedad (" + tripulante.getAntiguedad() + " anios)",
                HaberBase.remuneracion(tripulante.getCargo())
                        * tripulante.getAntiguedad()
                        * porcentaje(tripulante.getCargo()) / 100);
    }

    // Porcentaje de la remuneracion del cargo por cada anio de antiguedad
    private static double porcentaje(String cargo) {
        switch (cargo) {
            case Cargo.CAPITAN:   return 20;
            case Cargo.CONSEJERO: return 5;
            case Cargo.TENIENTE:  return 3;
            case Cargo.ALFEREZ:   return 0.5;
            default: throw new AssertionError("Cargo no contemplado: " + cargo);
        }
    }
}