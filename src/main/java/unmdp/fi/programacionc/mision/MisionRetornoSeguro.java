package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;

/**
 * M-03 Retorno seguro. Version simplificada de E1: su accion es simulada
 * (Aclaracion R4); el contenido real de la mision se define en E2.
 */
public class MisionRetornoSeguro extends Mision {

    private static final int COMBUSTIBLE_REQUERIDO = 4;
    private static final int ENERGIA_REQUERIDA = 0;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionRetornoSeguro(OperadorMision asistente) {
        super("M-03", "Retorno seguro", "Completar el regreso simulado", asistente);
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
     * Exito: la nave termina en estado operativo valido, es decir, con el motor disponible
     * y sin mantenimiento pendiente.
     */
    @Override
    protected ResultadoMision evaluar() {
        boolean operativa = getAsistente().estaDisponible() && !getAsistente().necesitaMantenimiento();
        return operativa ? ResultadoMision.EXITOSA : ResultadoMision.FALLIDA;
    }

    @Override
    protected void cerrar(ResultadoMision resultado, String motivo) {
        String mensaje;
        switch (resultado) {
            case EXITOSA:
                mensaje = "regreso completado, la nave quedo operativa";
                break;
            case FALLIDA:
                mensaje = "la nave regreso pero no quedo en estado operativo (requiere mantenimiento)";
                break;
            default: // RECHAZADA
                mensaje = "no se realizo: " + motivo;
        }
        getAsistente().registrarEvento(TipoEvento.MISION, getCodigo() + " cerrada [" + resultado + "] " + mensaje);
    }
}
