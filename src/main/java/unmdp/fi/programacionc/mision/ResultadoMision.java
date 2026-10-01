package unmdp.fi.programacionc.mision;

/**
 * Los tres finales posibles de una mision.
 * RECHAZADA: no paso la preparacion, la nave no se modifico.
 * FALLIDA:   se ejecuto, pero no cumplio su condicion de exito.
 * EXITOSA:   se ejecuto y cumplio su condicion de exito.
 */
public enum ResultadoMision {
    EXITOSA, FALLIDA, RECHAZADA
}