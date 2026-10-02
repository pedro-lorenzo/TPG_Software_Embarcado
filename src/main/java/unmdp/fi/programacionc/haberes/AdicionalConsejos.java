package unmdp.fi.programacionc.haberes;

public class AdicionalConsejos extends DecoratorHaber {

    private static final double PG_POR_CONSEJO = 2;

    /** pre -> consejos >= 0 */
    public AdicionalConsejos(ComponenteHaber componente, int consejos) {
        super(componente,
                "Adicional por consejos (" + consejos + ")",
                consejos * PG_POR_CONSEJO);
    }
}