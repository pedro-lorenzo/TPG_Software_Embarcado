package unmdp.fi.programacionc.tablero;

/** Lo que sabe hacer una nave propia frente a una enemiga (Double Dispatch, paso 1). */
public interface Combatiente {
    /** Resultado del combate desde el punto de vista de esta nave. */
    ResultadoBatalla enfrentar(NaveEnemiga enemiga);
}