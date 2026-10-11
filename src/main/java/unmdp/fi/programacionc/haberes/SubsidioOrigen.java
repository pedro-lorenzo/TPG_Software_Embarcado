package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;

/**
 * Subsidio mensual segun el planeta de origen del tripulante.
 */
public class SubsidioOrigen extends DecoratorHaber {

    /**
     * pre -> componente != null
     * pre -> tripulante != null
     */
    public SubsidioOrigen(ComponenteHaber componente, Tripulante tripulante) {
        super(componente,
                "Subsidio por origen (" + tripulante.getOrigen() + ")",
                subsidio(tripulante.getOrigen()));
    }

    /**
     * pre -> origen != null
     */
    private static double subsidio(Origen origen) {
        switch (origen) {
            case TERRICOLA: return 20;
            case VULCANO:   return 30;
            case MARCIANO:  return 18;
            default: throw new AssertionError("Origen no contemplado: " + origen);
        }
    }
}
