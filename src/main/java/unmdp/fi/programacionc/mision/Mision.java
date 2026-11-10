package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.excepciones.MisionNoEjecutableException;
import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;
import unmdp.fi.programacionc.motor.MotorWarp;

import java.util.ArrayList;
import java.util.List;

/**
 * Mision encomendada a un asistente de comando (Aclaracion R4).
 *
 * Patron Template Method: ejecutarCiclo() fija el orden preparar -> ejecutar -> evaluar -> cerrar
 * y es final, asi ninguna mision concreta puede alterarlo.
 *  - Pasos comunes a toda mision (privados): preparar() y ejecutar().
 *  - Ganchos que define cada mision concreta (abstractos): sus requisitos
 *    (getCombustibleRequerido, getEnergiaRequerida), su condicion de exito (evaluar) y su cierre (cerrar).
 * Agregar un tipo de mision es agregar una subclase: las existentes no se modifican (Abierto/Cerrado).
 *
 * La mision no conoce a la Nave: consulta y ordena solo a traves de OperadorMision
 * (Inversion de Dependencias). Cada instancia se encomienda a un asistente en particular.
 *
 * inv -> codigo, nombre y descripcion no nulos ni vacios
 * inv -> asistente != null
 */
public abstract class Mision {

    /** Desgaste que produce toda mision (Ficha de Inicio E1). */
    private static final int DESGASTE_PRODUCIDO = 4;

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final OperadorMision asistente;
    private final Tablero tablero;
    private final Destino destino;
    private Posicion objetivo;
    private boolean llegoAlDestino = false;
    private boolean finalizada = false;
    private List<Posicion> ruta = new ArrayList<>();
    private boolean destinoVerificado = false;

    /**
     * pre -> codigo, nombre y descripcion no nulos ni vacios
     * pre -> asistente != null
     */
    protected Mision(String codigo, String nombre, String descripcion, OperadorMision asistente, Tablero tablero, Destino destino) {
        assert codigo != null && !codigo.trim().isEmpty() : "El codigo de la mision no puede ser nulo ni vacio";
        assert nombre != null && !nombre.trim().isEmpty() : "El nombre de la mision no puede ser nulo ni vacio";
        assert descripcion != null && !descripcion.trim().isEmpty() : "La descripcion no puede ser nula ni vacia";
        assert asistente != null : "La mision debe encomendarse a un asistente";
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.asistente = asistente;
        this.tablero = tablero;
        this.destino = destino;
    }

    // ===================== TEMPLATE METHOD =====================

    /**
     * Ciclo completo de la mision.
     * Si la preparacion falla (nave destruida, mantenimiento pendiente o recursos insuficientes)
     * no se ejecuta ni se evalua: la mision se cierra como RECHAZADA y la nave no se modifica.
     * Cualquier otra excepcion no es un caso esperado y se propaga a quien ejecuto la mision.
     *
     * pre -> la mision no fue ejecutada antes
     * post -> la mision queda finalizada y su informe registrado en la bitacora del asistente
     * @return EXITOSA, FALLIDA o RECHAZADA
     */
    public final ResultadoMision ejecutarCiclo() {
        assert !finalizada : "La mision " + codigo + " ya fue ejecutada";

        int combustibleInicial = asistente.getCombustible();
        int energiaInicial = asistente.getEnergia();
        int desgasteInicial = asistente.getDesgaste();

        ResultadoMision resultado;
        String motivo = null;
        try {
            preparar();
            ejecutar();
            resultado = evaluar();
        } catch (MisionNoEjecutableException e) {
            resultado = ResultadoMision.RECHAZADA;
            motivo = e.getMessage();
            asistente.registrarEvento(TipoEvento.ERROR, codigo + " rechazada: " + motivo);
        }
        String observaciones = (resultado == ResultadoMision.RECHAZADA) ? motivo : explicarResultado(resultado);
        // Informe (Aclaracion R5): consumos del recorrido, medidos despues de evaluar y antes de cerrar
        int combustibleConsumido = combustibleInicial - asistente.getCombustible();
        int energiaConsumida = energiaInicial - asistente.getEnergia();
        int desgasteProducido = asistente.getDesgaste() - desgasteInicial;

        // Cierre: impacta el estado; su balance es la diferencia de antes y despues de cerrar()
        int combustibleAntes = asistente.getCombustible();
        int energiaAntes = asistente.getEnergia();
        int desgasteAntes = asistente.getDesgaste();
        cerrar(resultado, motivo);
        int gemasObtenidas = gemasObtenidas();
        int gemasPerdidas = gemasPerdidas();

        InformeMision informe = new InformeMision(codigo, nombre, resultado, ruta,
                combustibleConsumido, energiaConsumida, desgasteProducido,
                asistente.getCombustible() - combustibleAntes,
                asistente.getEnergia() - energiaAntes,
                asistente.getDesgaste() - desgasteAntes,
                gemasObtenidas, gemasPerdidas,
                asistente.getEstadoMotor(), observaciones);
        asistente.registrarInforme(informe);
        asistente.registrarEvento(TipoEvento.MISION, codigo + " informe: " + informe.resumen());

        this.finalizada = true;
        return resultado;
    }

    // ===================== PASOS COMUNES =====================
    private void preparar() {
        asistente.registrarEvento(TipoEvento.MISION, codigo + ": preparando mision");

        if (asistente.estaDestruida()) {
            throw new MisionNoEjecutableException("la nave esta destruida");
        }
        if (requiereMantenimientoAlDia() && asistente.necesitaMantenimiento()) {
            throw new MisionNoEjecutableException(
                    "la nave requiere mantenimiento (desgaste " + asistente.getDesgaste() + ")");
        }

        // Calculo de ruta (radar de largo alcance): queda almacenada
        objetivo = calcularObjetivo(tablero);
        ruta = Ruta.calcular(tablero.getPosicionNave(), objetivo);

        // Recursos minimos: el despegue mas un salto simple por casillero (el peor caso, sin warp)
        int combustibleMinimo = getCombustibleRequerido() + ruta.size() * MotorWarp.COMBUSTIBLE_SALTO;
        if (asistente.getCombustible() < combustibleMinimo) {
            throw new MisionNoEjecutableException("combustible insuficiente (requiere " + combustibleMinimo
                    + ": " + getCombustibleRequerido() + " de despegue + " + ruta.size()
                    + " saltos de " + MotorWarp.COMBUSTIBLE_SALTO + ", hay " + asistente.getCombustible() + ")");
        }
        if (asistente.getEnergia() < getEnergiaRequerida()) {
            throw new MisionNoEjecutableException("energia insuficiente (requiere "
                    + getEnergiaRequerida() + ", hay " + asistente.getEnergia() + ")");
        }
        asistente.registrarEvento(TipoEvento.MISION, codigo + ": preparacion correcta, ruta de "
                + ruta.size() + " pasos hacia " + objetivo.getX() + "," + objetivo.getY());
    }

    /**
     * Consume los requisitos de la mision y realiza el salto (Aclaracion R4).
     * El salto es un salto simple del motor; su estado queda donde lo deje la transicion.
     *
     * pre -> preparar() termino sin excepcion (lo garantiza el template)
     */
    private void ejecutar() {
        asistente.consumir(getCombustibleRequerido(), getEnergiaRequerida(), DESGASTE_PRODUCIDO);
        Travesia travesia = new Travesia(tablero, destino, asistente, asistente);
        llegoAlDestino = travesia.recorrer(ruta);
        destinoVerificado = llegoAlDestino && verificarEnDestino();
        if (llegoAlDestino && !destinoVerificado) {
            asistente.registrarEvento(TipoEvento.MISION, codigo + ": llego al destino pero no conserva los recursos a entregar");
        }
        asistente.registrarEvento(TipoEvento.MISION, codigo + ": " + descripcion);
    }

    /**
     * Explica por que una mision no tuvo exito, para las observaciones del informe.
     * post -> null si la mision no fue FALLIDA
     */
    private String explicarResultado(ResultadoMision resultado) {
        if (resultado != ResultadoMision.FALLIDA) {
            return null;
        }
        if (asistente.estaDestruida()) {
            return "la nave fue destruida: " + asistente.getMotivoDestruccion();
        }
        if (!llegoAlDestino) {
            return "no llego al destino: se quedo sin combustible para seguir saltando";
        }
        if (!destinoVerificado) {
            return "llego al destino pero no conservaba los recursos a entregar";
        }
        return "no se cumplio la condicion de exito de la mision";
    }
    // ===================== GANCHOS DE CADA MISION =====================

    /**
     * Combustible que consume esta mision.
     * post -> resultado >= 0
     */
    protected abstract int getCombustibleRequerido();

    /**
     * Energia que consume esta mision.
     * post -> resultado >= 0
     */
    protected abstract int getEnergiaRequerida();

    /**
     * Si es true (por defecto), la mision se rechaza cuando la nave necesita mantenimiento.
     */
    protected boolean requiereMantenimientoAlDia() {
        return true;
    }

    /**
     * Revisa la condicion de exito propia de la mision.
     * post -> devuelve EXITOSA o FALLIDA (nunca RECHAZADA ni null)
     */
    protected abstract ResultadoMision evaluar();

    /**
     * Registra el cierre propio de la mision en la bitacora del asistente.
     * Se llama siempre, tambien cuando la mision fue rechazada en la preparacion.
     *
     * pre -> resultado != null
     * pre -> motivo != null si y solo si resultado == RECHAZADA
     */
    protected abstract void cerrar(ResultadoMision resultado, String motivo);

    /** Gemas ganadas por la mision; se consulta despues de cerrar(). Por defecto 0. */
    protected int gemasObtenidas() { return 0; }

    /** Gemas que no entraron en el tanque al canjear; se consulta despues de cerrar(). Por defecto 0. */
    protected int gemasPerdidas() { return 0; }

    /**
     * Calcula la coordenada de destino de esta mision (radar de largo alcance).
     * Cada mision concreta decide cual es su objetivo.
     *
     * pre -> tablero != null
     * post -> resultado != null
     * post -> resultado esta dentro de los limites del tablero
     */
    protected abstract Posicion calcularObjetivo(Tablero tablero);

    /**
     * Verificacion en destino (Fase 2): si la mision exige entregar recursos, comprueba que la nave
     * todavia los conserve. No descuenta nada: el descuento efectivo se hace en cerrar().
     * Por defecto no exige nada.
     */
    protected boolean verificarEnDestino() {
        return true;
    }

    protected boolean destinoVerificado() { return destinoVerificado; }

    // ===================== ACCESO PARA LAS SUBCLASES =====================

    /** Las subclases hablan con la nave unicamente a traves del asistente. */
    protected OperadorMision getAsistente() { return asistente; }

    protected boolean llegoAlDestino() { return llegoAlDestino; }

    // ===================== CONSULTAS =====================

    public String getCodigo()       { return codigo; }
    public String getNombre()       { return nombre; }
    public String getDescripcion()  { return descripcion; }
    public boolean estaFinalizada() { return finalizada; }
}
