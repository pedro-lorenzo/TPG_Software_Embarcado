package unmdp.fi.programacionc.haberes;

import java.util.List;

// Componente del patron Decorator
public interface ComponenteHaber {
    /** Total acumulado hasta este componente (en PG). */
    double getImporte();

    /** Aporte de este concepto solamente (en PG). */
    double getAporte();

    /** Nombre de este concepto. */
    String getConcepto();

    /** Componente que este envuelve, o null si es el haber base. */
    ComponenteHaber getInterno();
}