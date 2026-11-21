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
    private final int combustibleGanado;
    private final int energiaGanada;
    private final int combustiblePerdido;
    private final int energiaPerdida;
    private final int cierreCombustible;
    private final int cierreEnergia;
    private final int cierreDesgaste;
    private final int gemasObtenidas;
    private final int gemasPerdidas;
    private final String estadoMotor;
    private final String observaciones;

    /**
     * Copia los valores del balance: el informe no queda atado a un objeto que se puede seguir modificando.
     * pre -> balance != null
     */
    public InformeMision(String codigo, String nombre, ResultadoMision resultado, List<Posicion> ruta,
                         BalanceRecursos balance,
                         int cierreCombustible, int cierreEnergia, int cierreDesgaste,
                         int gemasObtenidas, int gemasPerdidas,
                         String estadoMotor, String observaciones) {
        assert balance != null : "El informe necesita el balance de la mision";
        this.codigo = codigo;
        this.nombre = nombre;
        this.resultado = resultado;
        this.ruta = Collections.unmodifiableList(new ArrayList<>(ruta));
        this.combustibleConsumido = balance.getCombustibleConsumido();
        this.energiaConsumida = balance.getEnergiaConsumida();
        this.desgasteProducido = balance.getDesgasteProducido();
        this.combustibleGanado = balance.getCombustibleGanado();
        this.energiaGanada = balance.getEnergiaGanada();
        this.combustiblePerdido = balance.getCombustiblePerdido();
        this.energiaPerdida = balance.getEnergiaPerdida();
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
    public int getCombustibleGanado()       { return combustibleGanado; }
    public int getEnergiaGanada()           { return energiaGanada; }
    public int getCombustiblePerdido()      { return combustiblePerdido; }
    public int getEnergiaPerdida()          { return energiaPerdida; }
    public int getGemasObtenidas()          { return gemasObtenidas; }
    public int getGemasPerdidas()           { return gemasPerdidas; }
    public String getEstadoMotor()          { return estadoMotor; }
    public String getObservaciones()        { return observaciones; }

    /** Texto de una linea para la bitacora y para el historial de la pantalla. */
    public String resumen() {
        return nombre
                + " | resultado=" + resultado
                + " | ruta=" + ruta.size() + " pasos"
                + " | consumido: combustible " + combustibleConsumido
                + ", energia " + energiaConsumida + ", desgaste +" + desgasteProducido
                + " | ganado en estaciones: combustible " + combustibleGanado + ", energia " + energiaGanada
                + " | perdido por impactos: combustible " + combustiblePerdido + ", energia " + energiaPerdida
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