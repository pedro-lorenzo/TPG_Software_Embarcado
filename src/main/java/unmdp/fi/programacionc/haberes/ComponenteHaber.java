package unmdp.fi.programacionc.haberes;

/**
 * Componente del patron Decorator (E1-08): un concepto del haber mensual de un tripulante.
 * El haber completo es una cadena: HaberBase en el centro, envuelto por decoradores.
 * Recorriendo getInterno() se obtiene el aporte de cada concepto por separado.
 */
public interface ComponenteHaber {

    /** Total acumulado hasta este componente, en PG. */
    double getImporte();

    /** Aporte de este concepto solamente, en PG. */
    double getAporte();

    /** Nombre de este concepto. */
    String getConcepto();

    /** Componente que este envuelve, o null si es el haber base. */
    ComponenteHaber getInterno();
}
