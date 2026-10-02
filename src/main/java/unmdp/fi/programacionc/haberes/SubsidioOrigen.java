package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;

public class SubsidioOrigen extends DecoratorHaber {

    public SubsidioOrigen(ComponenteHaber componente, Tripulante tripulante) {
        super(componente, new ConceptoHaber(
                "Subsidio por origen (" + tripulante.getOrigen() + ")",
                subsidio(tripulante.getOrigen())));
    }

    private static double subsidio(String origen) {
        switch (origen) {
            case Origen.TERRICOLA: return 20;
            case Origen.VULCANO:   return 30;
            case Origen.MARCIANO:  return 18;
            default: throw new AssertionError("Origen no contemplado: " + origen);
        }
    }
}