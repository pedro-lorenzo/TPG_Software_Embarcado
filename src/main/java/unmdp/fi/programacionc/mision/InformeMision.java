package unmdp.fi.programacionc.mision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unmdp.fi.programacionc.destinoytablero.Posicion;

/**
 * Informe final de una mision. Inmutable.
 */
public class InformeMision {

    private final String codigo;
    private final String nombre;
    private final ResultadoMision resultado;
    private final List<Posicion> ruta;
    private final int combustibleConsumido;
    private final int energiaConsumida;
    private final int desgasteProducido;
    private final int cierreCombustible;
    private final int cierreEnergia;
    private final int cierreDesgaste;
    private final int gemasObtenidas;
    private final int gemasPerdidas;
    private final String estadoMotor;
    private final String observaciones;

    public InformeMision(String codigo, String nombre, ResultadoMision resultado, List<Posicion> ruta,
                         int combustibleConsumido, int energiaConsumida, int desgasteProducido,
                         int cierreCombustible, int cierreEnergia, int cierreDesgaste,
                         int gemasObtenidas, int gemasPerdidas,
                         String estadoMotor, String observaciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.resultado = resultado;
        this.ruta = Collections.unmodifiableList(new ArrayList<>(ruta));
        this.combustibleConsumido = combustibleConsumido;
        this.energiaConsumida = energiaConsumida;
        this.desgasteProducido = desgasteProducido;
        this.cierreCombustible = cierreCombustible;
        this.cierreEnergia = cierreEnergia;
        this.cierreDesgaste = cierreDesgaste;
        this.gemasObtenidas = gemasObtenidas;
        this.gemasPerdidas = gemasPerdidas;
        this.estadoMotor = estadoMotor;
        this.observaciones = observaciones;
    }

    public String getCodigo()               { return codigo; }
    public String getNombre()               { return nombre; }
    public ResultadoMision getResultado()   { return resultado; }
    public List<Posicion> getRuta()         { return ruta; }
    public int getCombustibleConsumido()    { return combustibleConsumido; }
    public int getEnergiaConsumida()        { return energiaConsumida; }
    public int getDesgasteProducido()       { return desgasteProducido; }
    public int getGemasObtenidas()          { return gemasObtenidas; }
    public int getGemasPerdidas()           { return gemasPerdidas; }
    public String getEstadoMotor()         { return estadoMotor; }
    public String getObservaciones()        { return observaciones; }

    /** Texto de una linea para la bitacora y para el historial de la pantalla. */
    public String resumen() {
        return nombre
                + " | resultado=" + resultado
                + " | ruta=" + ruta.size() + " pasos"
                + " | combustible consumido=" + combustibleConsumido
                + " | energia consumida=" + energiaConsumida
                + " | desgaste producido=" + desgasteProducido
                + " | cierre: combustible " + signado(cierreCombustible)
                + ", energia " + signado(cierreEnergia)
                + ", desgaste " + signado(cierreDesgaste)
                + ", gemas obtenidas=" + gemasObtenidas + ", gemas perdidas=" + gemasPerdidas
                + " | motor=" + estadoMotor
                + " | observaciones=" + (observaciones == null ? "ninguna" : observaciones);
    }

    private static String signado(int valor) {
        return (valor >= 0 ? "+" : "") + valor;
    }
}
