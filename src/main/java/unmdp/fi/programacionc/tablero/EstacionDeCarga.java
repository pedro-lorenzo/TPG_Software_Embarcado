package unmdp.fi.programacionc.tablero;

/** Ayuda: recarga un monto fijo de combustible y energia. */
public class EstacionDeCarga implements ElementoDelUniverso {

    public static final int RECARGA_COMBUSTIBLE = 25;
    public static final int RECARGA_ENERGIA = 25;

    @Override public String interactuar(NavePropia nave) {
        assert nave != null : "La nave no puede ser nula";
        nave.recargar(RECARGA_COMBUSTIBLE, RECARGA_ENERGIA);
        return "Estacion de carga: +" + RECARGA_COMBUSTIBLE + " combustible, +" + RECARGA_ENERGIA + " energia";
    }

    @Override public char getSimbolo() { return 'E'; }
    @Override public boolean esObstaculo() { return false; }
}