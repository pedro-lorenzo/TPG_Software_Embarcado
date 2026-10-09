package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;

/**
 * M-02 Recoleccion. Version simplificada de E1: su accion es simulada
 * (Aclaracion R4); el contenido real de la mision se define en E2.
 */
public class MisionRecoleccion extends Mision {

    private static final int COMBUSTIBLE_REQUERIDO = 4;
    private static final int ENERGIA_REQUERIDA = 5;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionRecoleccion(OperadorMision asistente) {
        super("M-02", "Recoleccion", "Llegar al punto simulado y obtener datos o muestra", asistente);
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
     * Exito: la muestra queda registrada como obtenida. Como la recoleccion es simulada,
     * se obtiene si el salto termino y la nave quedo operativa (motor disponible).
     */
    @Override
    protected ResultadoMision evaluar() {
        return getAsistente().estaDisponible() ? ResultadoMision.EXITOSA : ResultadoMision.FALLIDA;
    }

    @Override
    protected void cerrar(ResultadoMision resultado, String motivo) {
        String mensaje;
        switch (resultado) {
            case EXITOSA:
                mensaje = "muestra registrada como obtenida";
                break;
            case FALLIDA:
                mensaje = "no se pudo registrar la muestra";
                break;
            default: // RECHAZADA
                mensaje = "no se realizo: " + motivo;
        }
        getAsistente().registrarEvento(TipoEvento.MISION, getCodigo() + " cerrada [" + resultado + "] " + mensaje);
    }
}
