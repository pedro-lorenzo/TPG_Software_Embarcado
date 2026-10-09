package unmdp.fi.programacionc.haberes;

/**
 * Adicional del consejero: 2 PG por cada consejo registrado en el periodo liquidado.
 */
public class AdicionalConsejos extends DecoratorHaber {

    private static final double PG_POR_CONSEJO = 2;

    /**
     * pre -> componente != null
     * pre -> consejos >= 0 (si no, el aporte seria negativo y lo rechaza DecoratorHaber)
     */
    public AdicionalConsejos(ComponenteHaber componente, int consejos) {
        super(componente,
                "Adicional por consejos (" + consejos + ")",
                consejos * PG_POR_CONSEJO);
    }
}
