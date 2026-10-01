package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;

// Mision M-03 - Retorno seguro
public class MisionRetornoSeguro extends Mision {

    private static final int ENERGIA_REQUERIDA = 0;
    private static final int UMBRAL_MANTENIMIENTO = 80;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionRetornoSeguro(OperadorMision asistente) {
        super("M-03", "Retorno seguro", "Completar el regreso simulado", asistente);
    }

    @Override
    protected int getEnergiaRequerida() {
        return ENERGIA_REQUERIDA;
    }

    /**
     * Condicion de exito (Ficha E1): la nave finaliza en estado operativo valido.
     * Operativa = motor disponible y sin necesidad de mantenimiento.
     */
    @Override
    protected ResultadoMision evaluar() {
        boolean operativa = getAsistente().estaDisponible() && getAsistente().getDesgaste() < UMBRAL_MANTENIMIENTO;
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