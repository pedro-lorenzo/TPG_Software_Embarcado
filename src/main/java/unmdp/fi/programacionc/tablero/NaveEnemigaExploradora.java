// NaveEnemigaExploradora.java
package unmdp.fi.programacionc.tablero;

public class NaveEnemigaExploradora extends NaveEnemiga {

    public NaveEnemigaExploradora() { super("Exploradora enemiga"); }

    @Override public ResultadoBatalla recibirDeExplorador() { return ResultadoBatalla.PAZ; }
    @Override public ResultadoBatalla recibirDeCarguero()   { return ResultadoBatalla.PIERDE; }
    @Override public ResultadoBatalla recibirDeCombate()    { return ResultadoBatalla.GANA; }

    @Override public char getSimbolo() { return 'x'; }
}