package unmdp.fi.programacionc.haberes;

/**
 * Decorator base: envuelve a otro componente y le suma un concepto.
 * Cada decorador concreto solo calcula su aporte; el total se obtiene encadenando.
 * Asi se evita una subclase por cada combinacion de cargo, origen y antiguedad.
 *
 * inv -> componente != null
 * inv -> concepto no nulo ni vacio
 * inv -> aporte finito y >= 0
 */
public abstract class DecoratorHaber implements ComponenteHaber {

    private final ComponenteHaber componente;
    private final String concepto;
    private final double aporte;

    /**
     * pre -> componente != null
     * pre -> concepto no nulo ni vacio
     * pre -> aporte finito y >= 0
     */
    protected DecoratorHaber(ComponenteHaber componente, String concepto, double aporte) {
        assert componente != null : "El componente a decorar no puede ser nulo";
        assert concepto != null && !concepto.trim().isEmpty() : "El concepto necesita un nombre";
        assert !Double.isNaN(aporte) && !Double.isInfinite(aporte) && aporte >= 0 : "Importe invalido: " + aporte;
        this.componente = componente;
        this.concepto = concepto;
        this.aporte = aporte;
    }

    @Override public double getImporte()          { return componente.getImporte() + aporte; }
    @Override public double getAporte()           { return aporte; }
    @Override public String getConcepto()         { return concepto; }
    @Override public ComponenteHaber getInterno() { return componente; }
}
