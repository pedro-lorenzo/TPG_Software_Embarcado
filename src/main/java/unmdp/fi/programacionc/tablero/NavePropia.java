package unmdp.fi.programacionc.tablero;

/**
 * La nave propia, tal como la ven los elementos del universo: lo que pueden preguntarle
 * y hacerle. Vive en tablero porque los elementos definen lo que necesitan
 * (Inversion de Dependencias).
 */
public interface NavePropia {
    ResultadoBatalla enfrentar(NaveEnemiga enemiga);

    /** Descuenta energia; si llega a 0 o menos, la nave queda destruida. */
    void recibirImpactoDeEnergia(int energia);

    void recargar(int combustible, int energia);

    void destruir(String motivo);
}