package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.comando.AsistenteComando;
import unmdp.fi.programacionc.excepciones.MisionNoEjecutableException;

/**
 * Patron Template Method: ejecutarCiclo() fija el orden preparar -> ejecutar -> evaluar -> cerrar
 * y ninguna subclase puede alterarlo (es final).
 *
 * Partes comunes (final): preparar() y ejecutar().
 * Ganchos de cada mision concreta (abstractos): getEnergiaRequerida(), evaluar() y cerrar().
 *
 * La mision se encomienda a UN asistente en particular (llega por el constructor) y solo
 * habla con la nave a traves de el. No conoce a la Nave.
 *
 * inv -> asistente != null
 * inv -> codigo y nombre != null y no vacios
 */
public abstract class Mision {

    // Costos comunes a toda mision (Ficha de Inicio E1, punto 5)
    protected static final int COMBUSTIBLE_REQUERIDO = 4;
    protected static final int DESGASTE_PRODUCIDO = 4;
    private static final int TOPE_DESGASTE = 100;

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final AsistenteComando asistente;

    private boolean finalizada = false;

    /**
     * pre -> codigo, nombre y descripcion != null y no vacios
     * pre -> asistente != null
     */
    protected Mision(String codigo, String nombre, String descripcion, AsistenteComando asistente) {
        assert codigo != null && !codigo.trim().isEmpty() : "El codigo de la mision no puede ser nulo ni vacio";
        assert nombre != null && !nombre.trim().isEmpty() : "El nombre de la mision no puede ser nulo ni vacio";
        assert descripcion != null && !descripcion.trim().isEmpty() : "La descripcion no puede ser nula ni vacia";
        assert asistente != null : "La mision debe encomendarse a un asistente";
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.asistente = asistente;
    }

    // ===================== TEMPLATE METHOD =====================

    /**
     * Ciclo completo de la mision.
     * Si la preparacion falla (caso esperado: faltan recursos o el motor no esta disponible),
     * se saltea la parte ejecutable y la mision se cierra igual, como RECHAZADA.
     * Cualquier otra excepcion no es un caso esperado y se propaga al asistente.
     *
     * pre -> la mision no se ejecuto antes
     * post -> la mision queda finalizada y su resultado registrado en la bitacora
     */
    public final ResultadoMision ejecutarCiclo() {
        assert !finalizada : "La mision " + codigo + " ya fue ejecutada";

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
        cerrar(resultado, motivo);
        this.finalizada = true;
        return resultado;
    }

    // ===================== PASOS COMUNES =====================

    /**
     * Verifica que la mision se pueda realizar ANTES de tocar nada.
     * Comprueba cada condicion por separado para informar el motivo exacto.
     *
     * post -> no modifica la nave
     * @throws MisionNoEjecutableException si alguna condicion no se cumple
     */
    private void preparar() {
        asistente.registrarEvento(TipoEvento.MISION, codigo + ": preparando mision");

        if (!asistente.estaDisponible()) {
            throw new MisionNoEjecutableException(
                    "el motor no esta disponible (estado: " + asistente.getEstadoMotor() + ")");
        }
        if (asistente.getCombustible() < COMBUSTIBLE_REQUERIDO) {
            throw new MisionNoEjecutableException("combustible insuficiente (requiere "
                    + COMBUSTIBLE_REQUERIDO + ", hay " + asistente.getCombustible() + ")");
        }
        if (asistente.getEnergia() < getEnergiaRequerida()) {
            throw new MisionNoEjecutableException("energia insuficiente (requiere "
                    + getEnergiaRequerida() + ", hay " + asistente.getEnergia() + ")");
        }
        if (asistente.getDesgaste() + DESGASTE_PRODUCIDO > TOPE_DESGASTE) {
            throw new MisionNoEjecutableException("la mision dejaria el desgaste fuera de rango (actual "
                    + asistente.getDesgaste() + ")");
        }
        // DECISION PENDIENTE DEL GRUPO: ¿una nave que necesita mantenimiento (desgaste >= 80)
        // puede salir de mision? Si la respuesta es no, va un chequeo mas aca.

        asistente.registrarEvento(TipoEvento.MISION, codigo + ": preparacion correcta");
    }

    /**
     * Consume los recursos y realiza el salto. Es igual para todas las misiones.
     * Se consume antes de saltar; como preparar() verifico motor y recursos,
     * ninguna de estas ordenes deberia fallar. Si alguna falla es un error y se propaga.
     *
     * pre -> preparar() termino sin excepcion (lo garantiza el template)
     */
    private void ejecutar() {
        asistente.consumir(COMBUSTIBLE_REQUERIDO, getEnergiaRequerida(), DESGASTE_PRODUCIDO);
        asistente.prepararSalto();
        asistente.saltar();
        // Aclaracion R3: por ahora, al terminar el salto se vuelve directo a Disponible
        asistente.finalizarSalto();
        asistente.registrarEvento(TipoEvento.MISION, codigo + ": " + descripcion);
    }

    // ===================== GANCHOS DE CADA MISION =====================

    /** Energia que consume la accion final de esta mision (M-01: 5, M-02: 5, M-03: 0). */
    protected abstract int getEnergiaRequerida();

    /**
     * Revisa la condicion de exito propia de la mision (columna "Condicion de exito" de la Ficha).
     * post -> devuelve EXITOSA o FALLIDA (nunca RECHAZADA ni null)
     */
    protected abstract ResultadoMision evaluar();

    /**
     * Registra el cierre propio de la mision en la bitacora del asistente.
     * Se llama SIEMPRE, tambien cuando la mision fue rechazada en preparar().
     *
     * @param resultado EXITOSA, FALLIDA o RECHAZADA (nunca null)
     * @param motivo    motivo del rechazo si resultado == RECHAZADA; null en otro caso
     */
    protected abstract void cerrar(ResultadoMision resultado, String motivo);

    // ===================== ACCESO PARA LAS SUBCLASES =====================

    /** Las subclases hablan con la nave unicamente a traves del asistente. */
    protected AsistenteComando getAsistente() { return asistente; }

    // ===================== CONSULTAS =====================

    public String getCodigo()      { return codigo; }
    public String getNombre()      { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean estaFinalizada() { return finalizada; }
}