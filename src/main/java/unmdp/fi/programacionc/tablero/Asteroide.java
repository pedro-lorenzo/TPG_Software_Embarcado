package unmdp.fi.programacionc.tablero;

/** Obstaculo: al chocar descuenta un monto fijo de energia. */
public class Asteroide implements ElementoDelUniverso {

    public static final int DANIO_ENERGIA = 20;

    @Override public String interactuar(NavePropia nave) {
        assert nave != null : "La nave no puede ser nula";
        nave.recibirImpactoDeEnergia(DANIO_ENERGIA);
        return "Impacto con asteroide: -" + DANIO_ENERGIA + " de energia";
    }

    @Override public char getSimbolo() { return 'A'; }
}