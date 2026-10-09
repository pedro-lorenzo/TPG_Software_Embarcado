package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;

// Mision M-01 - Intercepcion y asistencia
public class MisionIntercepcion extends Mision {

    private static final int ENERGIA_REQUERIDA = 5;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionIntercepcion(OperadorMision asistente) {
        super("M-01", "Intercepcion y asistencia", "Llegar al objetivo simulado y realizar la asistencia", asistente);
    }

    @Override
    protected int getEnergiaRequerida() {
        return ENERGIA_REQUERIDA;
    }

    /**
     * Condicion de exito (Ficha E1): la asistencia se completa con recursos suficientes.
     * Si llegamos aca, ejecutar() ya consumio los recursos sin error; se confirma
     * ademas que la nave quedo operativa (motor disponible).
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
}