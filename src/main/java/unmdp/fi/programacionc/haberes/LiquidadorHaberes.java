package unmdp.fi.programacionc.haberes;

import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;

public final class LiquidadorHaberes {

    private LiquidadorHaberes() { }

    /**
     * pre -> tripulante != null
     * pre -> consejos >= 0 (solo se usa si el tripulante es consejero; para los demas se ignora)
     * post -> devuelve la cadena de decoradores; su importe es el total y sus conceptos el desglose
     */
    public static ComponenteHaber liquidar(Tripulante tripulante, int consejos) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        assert consejos >= 0 : "La cantidad de consejos no puede ser negativa";

        ComponenteHaber haber = new HaberBase(tripulante);
        haber = new AdicionalAntiguedad(haber, tripulante);
        if (Cargo.CONSEJERO.equals(tripulante.getCargo())) {
            haber = new AdicionalConsejos(haber, consejos);
        }
        haber = new SubsidioOrigen(haber, tripulante);
        return haber;
    }
}