package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;
import java.util.Random;
/**
 * M-01 Intercepcion y asistencia. Version simplificada de E1: su accion es simulada
 * (Aclaracion R4); el contenido real de la mision se define en E2.
 */
public class MisionIntercepcion extends Mision {

    private static final int COMBUSTIBLE_REQUERIDO = 4;
    private static final int ENERGIA_REQUERIDA = 5;
    private static final int PEDIDO_MAXIMO = 50;

    private final int pedidoCombustible;
    private final int pedidoEnergia;

    /**
     * pre -> asistente != null (lo verifica Mision)
     */
    public MisionIntercepcion(OperadorMision asistente, Tablero tablero, Destino destino) {
        this(asistente, new Random(), tablero, destino);
    }

    public MisionIntercepcion(OperadorMision asistente, Random azar, Tablero tablero, Destino destino) {
        super("M-01", "Intercepcion y asistencia", "Llegar a la nave aliada y transferirle los recursos de auxilio",
                asistente, tablero, destino);
        assert azar != null : "El generador aleatorio no puede ser nulo";
        this.pedidoCombustible = azar.nextInt(PEDIDO_MAXIMO + 1);
        this.pedidoEnergia = azar.nextInt(PEDIDO_MAXIMO + 1);
    }

    @Override
    protected int getCombustibleRequerido() {
        return COMBUSTIBLE_REQUERIDO;
    }

    @Override
    protected int getEnergiaRequerida() {
        return ENERGIA_REQUERIDA;
    }

    @Override
    protected boolean verificarEnDestino() {
        return getAsistente().getCombustible() >= pedidoCombustible
                && getAsistente().getEnergia() >= pedidoEnergia;
    }
    /**
     * Exito: la asistencia se completo con recursos suficientes (ya consumidos en ejecutar())
     * y la nave no fue destruida.
     */
    @Override
    protected ResultadoMision evaluar() {
        return destinoVerificado() ? ResultadoMision.EXITOSA : ResultadoMision.FALLIDA;
    }


    @Override
    protected void cerrar(ResultadoMision resultado, String motivo) {
        String mensaje;
        switch (resultado) {
            case EXITOSA:
                getAsistente().consumir(pedidoCombustible, pedidoEnergia, 0);
                mensaje = "se transfirieron " + pedidoCombustible + " de combustible y "
                        + pedidoEnergia + " de energia a la nave aliada";
                break;
            case FALLIDA:
                mensaje = "no se pudo completar la asistencia (pedido: " + pedidoCombustible
                        + " de combustible y " + pedidoEnergia + " de energia)";
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
