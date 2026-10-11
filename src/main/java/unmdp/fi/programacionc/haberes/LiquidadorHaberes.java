package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;

/**
 * Arma la cadena de decoradores que corresponde a cada tripulante:
 * base por cargo + antiguedad + consejos (solo consejero) + subsidio por origen.
 */
public final class LiquidadorHaberes {

    private LiquidadorHaberes() { }   // solo metodos estaticos, no se instancia

    /**
     * pre -> tripulante != null
     * pre -> consejos >= 0 (solo se usa si el tripulante es consejero; para los demas se ignora)
     * post -> devuelve la cadena de decoradores: getImporte() es el total y recorriendo
     *         getInterno() se obtiene el aporte de cada concepto
     */
    public static ComponenteHaber liquidar(Tripulante tripulante, int consejos) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        assert consejos >= 0 : "La cantidad de consejos no puede ser negativa";

        ComponenteHaber haber = new HaberBase(tripulante);
        haber = new AdicionalAntiguedad(haber, tripulante);
        if (tripulante.getCargo() == Cargo.CONSEJERO) {
            haber = new AdicionalConsejos(haber, consejos);
        }
        haber = new SubsidioOrigen(haber, tripulante);
        return haber;
    }
}
