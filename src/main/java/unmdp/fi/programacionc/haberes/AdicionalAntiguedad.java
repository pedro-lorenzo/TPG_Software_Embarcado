package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;

/**
 * Adicional por antiguedad: porcentaje por cada anio, calculado sobre la remuneracion
 * del cargo (no sobre el total acumulado).
 */
public class AdicionalAntiguedad extends DecoratorHaber {

    /**
     * pre -> componente != null
     * pre -> tripulante != null
     */
    public AdicionalAntiguedad(ComponenteHaber componente, Tripulante tripulante) {
        super(componente,
                "Adicional por antiguedad (" + tripulante.getAntiguedad() + " anios)",
                HaberBase.remuneracion(tripulante.getCargo())
                        * tripulante.getAntiguedad()
                        * porcentaje(tripulante.getCargo()) / 100);
    }

    /**
     * Porcentaje de la remuneracion del cargo por cada anio de antiguedad.
     * pre -> cargo != null
     */
    private static double porcentaje(Cargo cargo) {
        switch (cargo) {
            case CAPITAN:   return 20;
            case CONSEJERO: return 5;
            case TENIENTE:  return 3;
            case ALFEREZ:   return 0.5;
            default: throw new AssertionError("Cargo no contemplado: " + cargo);
        }
    }
}
