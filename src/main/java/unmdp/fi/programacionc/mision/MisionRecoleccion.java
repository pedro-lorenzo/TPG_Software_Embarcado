package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;

// Mision M-02 - Recoleccion
public class MisionRecoleccion extends Mision {

    private static final int ENERGIA_REQUERIDA = 5;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionRecoleccion(OperadorMision asistente) {
        super("M-02", "Recoleccion", "Llegar al punto simulado y obtener datos o muestra", asistente);
    }

    @Override
    protected int getEnergiaRequerida() {
        return ENERGIA_REQUERIDA;
    }

    /**
     * Condicion de exito (Ficha E1): el elemento queda registrado como obtenido.
     * En E1 la recoleccion es simulada: si ejecutar() termino sin error, la muestra se obtuvo.
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