// NaveEnemigaCombate.java
package unmdp.fi.programacionc.tablero;

public class NaveEnemigaCombate extends NaveEnemiga {

    public NaveEnemigaCombate() { super("Nave de combate enemiga"); }

    @Override public ResultadoBatalla recibirDeExplorador() { return ResultadoBatalla.PIERDE; }
    @Override public ResultadoBatalla recibirDeCarguero()   { return ResultadoBatalla.GANA; }
    @Override public ResultadoBatalla recibirDeCombate()    { return ResultadoBatalla.PAZ; }

    @Override public char getSimbolo() { return 'k'; }
}