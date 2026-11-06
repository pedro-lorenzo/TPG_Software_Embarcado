package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;

/**
 * M-01 Intercepcion y asistencia. Version simplificada de E1: su accion es simulada
 * (Aclaracion R4); el contenido real de la mision se define en E2.
 */
public class MisionIntercepcion extends Mision {

    private static final int COMBUSTIBLE_REQUERIDO = 4;
    private static final int ENERGIA_REQUERIDA = 5;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionIntercepcion(OperadorMision asistente, Tablero tablero, Destino destino) {
        super("M-01", "Intercepcion y asistencia", "Llegar al objetivo simulado y realizar la asistencia", asistente, tablero, destino);
    }

    @Override
    protected int getCombustibleRequerido() {
        return COMBUSTIBLE_REQUERIDO;
    }

    @Override
    protected int getEnergiaRequerida() {
        return ENERGIA_REQUERIDA;
    }

    /**
     * Exito: la asistencia se completo con recursos suficientes (ya consumidos en ejecutar())
     * y la nave no fue destruida.
     */
    @Override
    protected ResultadoMision evaluar() {
        return !getAsistente().estaDestruida() ? ResultadoMision.EXITOSA : ResultadoMision.FALLIDA;
    }

    @Override
    protected void cerrar(ResultadoMision resultado, String motivo) {
        String mensaje;
        switch (resultado) {
            case EXITOSA:
                mensaje = "asistencia completada con recursos suficientes";
                break;
            case FALLIDA:
                mensaje = "la nave no quedo operativa despues de la asistencia";
                break;
            default: // RECHAZADA
                mensaje = "no se realizo: " + motivo;
        }
        getAsistente().registrarEvento(TipoEvento.MISION, getCodigo() + " cerrada [" + resultado + "] " + mensaje);
    }
    protected Posicion calcularObjetivo(Tablero tablero){
        return tablero.obtenerObjetivo();
    }
}
