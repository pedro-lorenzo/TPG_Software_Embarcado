package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;

import java.util.Random;

/**
 * M-02 Recoleccion. Version simplificada de E1: su accion es simulada
 * (Aclaracion R4); el contenido real de la mision se define en E2.
 */
public class MisionRecoleccion extends Mision {

    private static final int COMBUSTIBLE_REQUERIDO = 4;
    private static final int ENERGIA_REQUERIDA = 10;
    private static final int GEMAS_MINIMAS = 20;
    private static final int GEMAS_MAXIMAS = 100;

    private final Random azar;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionRecoleccion(OperadorMision asistente, Tablero tablero, Destino destino) {
        this(asistente, new Random(), tablero, destino);
    }

    /**
     * pre -> asistente != null (lo verifica Mision)
     * pre -> azar != null
     */
    public MisionRecoleccion(OperadorMision asistente, Random azar, Tablero tablero, Destino destino) {
        super("M-02", "Recoleccion", "Llegar al punto simulado y obtener datos o muestra", asistente, tablero, destino);
        assert azar != null : "El generador aleatorio no puede ser nulo";
        this.azar = azar;
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
        return llegoAlDestino() ? ResultadoMision.EXITOSA : ResultadoMision.FALLIDA;
    }

    @Override
    protected void cerrar(ResultadoMision resultado, String motivo) {
        String mensaje;
        switch (resultado) {
            case EXITOSA:
                int gemas = GEMAS_MINIMAS + azar.nextInt(GEMAS_MAXIMAS - GEMAS_MINIMAS + 1);
                int combustibleAntes = getAsistente().getCombustible();
                getAsistente().agregarGemas(gemas);
                getAsistente().canjearGemas();
                int canjeado = getAsistente().getCombustible() - combustibleAntes;
                mensaje = "muestra registrada como obtenida; gemas obtenidas: " + gemas
                        + ", canjeadas por " + canjeado + " de combustible";
                break;
            case FALLIDA:
                mensaje = "no se pudo registrar la muestra";
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