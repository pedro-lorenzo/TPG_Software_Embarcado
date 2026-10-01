package unmdp.fi.programacionc.bitacora;

// Clasifica los eventos de la Bitacora segun lo que pide E1-05:
// cambios del Motor Warp, ejecucion de misiones, operaciones sobre recursos y errores.
public enum TipoEvento {
    MOTOR,
    MISION,
    RECURSOS,
    ERROR,
    INFO
}
