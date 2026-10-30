// NaveEnemigaCarguero.java
package unmdp.fi.programacionc.tablero;

public class NaveEnemigaCarguero extends NaveEnemiga {

    public NaveEnemigaCarguero() { super("Carguero enemigo"); }

    @Override public ResultadoBatalla recibirDeExplorador() { return ResultadoBatalla.GANA; }
    @Override public ResultadoBatalla recibirDeCarguero()   { return ResultadoBatalla.PAZ; }
    @Override public ResultadoBatalla recibirDeCombate()    { return ResultadoBatalla.PIERDE; }

    @Override public char getSimbolo() { return 'c'; }
}