package unmdp.fi.programacionc.aplicacion;

/**
 * Quien quiere enterarse de que cambio algo en la sesion (se creo la nave, se agrego un
 * tripulante, ...). La sesion solo conoce esta interfaz: no sabe que del otro lado hay pantallas.
 */
public interface ObservadorSesion {

    void sesionActualizada();
}
