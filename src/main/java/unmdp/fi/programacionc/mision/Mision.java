package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.excepciones.MisionNoEjecutableException;

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

    private boolean finalizada = false;

    /**
     * pre -> codigo, nombre y descripcion no nulos ni vacios
     * pre -> asistente != null
     */
    protected Mision(String codigo, String nombre, String descripcion, OperadorMision asistente) {
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
     * Si la preparacion falla (motor no disponible, mantenimiento pendiente o recursos insuficientes)
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
        cerrar(resultado, motivo);

        // Informe de la mision (Aclaracion R5): la bitacora es el informe
        asistente.registrarEvento(TipoEvento.MISION, codigo + " informe: " + nombre
                + " | resultado=" + resultado
                + " | combustible consumido=" + (combustibleInicial - asistente.getCombustible())
                + " | energia consumida=" + (energiaInicial - asistente.getEnergia())
                + " | desgaste producido=" + (asistente.getDesgaste() - desgasteInicial)
                + " | motor=" + asistente.getEstadoMotor()
                + " | observaciones=" + (motivo == null ? "ninguna" : motivo));

        this.finalizada = true;
        return resultado;
    }

    // ===================== PASOS COMUNES =====================

    /**
     * Verifica que la mision se pueda realizar antes de modificar nada (Aclaracion R4).
     * Revisa cada condicion por separado para dejar el motivo exacto del rechazo.
     * El tope de desgaste no se revisa aca: lo protege Tanque, y con el desgaste por debajo
     * del umbral de mantenimiento el desgaste de una mision no lo alcanza.
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
        if (asistente.necesitaMantenimiento()) {
            throw new MisionNoEjecutableException(
                    "la nave requiere mantenimiento (desgaste " + asistente.getDesgaste() + ")");
        }
        if (asistente.getCombustible() < getCombustibleRequerido()) {
            throw new MisionNoEjecutableException("combustible insuficiente (requiere "
                    + getCombustibleRequerido() + ", hay " + asistente.getCombustible() + ")");
        }
        if (asistente.getEnergia() < getEnergiaRequerida()) {
            throw new MisionNoEjecutableException("energia insuficiente (requiere "
                    + getEnergiaRequerida() + ", hay " + asistente.getEnergia() + ")");
        }

        asistente.registrarEvento(TipoEvento.MISION, codigo + ": preparacion correcta");
    }

    /**
     * Consume los requisitos de la mision y realiza el salto (Aclaracion R4).
     * Como todavia no se modela el paso del tiempo, el salto termina y el motor vuelve
     * a Disponible (Aclaracion R3).
     *
     * pre -> preparar() termino sin excepcion (lo garantiza el template)
     */
    private void ejecutar() {
        asistente.consumir(getCombustibleRequerido(), getEnergiaRequerida(), DESGASTE_PRODUCIDO);
        asistente.prepararSalto();
        asistente.saltar();
        asistente.finalizarSalto();
        asistente.registrarEvento(TipoEvento.MISION, codigo + ": " + descripcion);
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

    // ===================== ACCESO PARA LAS SUBCLASES =====================

    /** Las subclases hablan con la nave unicamente a traves del asistente. */
    protected OperadorMision getAsistente() { return asistente; }

    // ===================== CONSULTAS =====================

    public String getCodigo()       { return codigo; }
    public String getNombre()       { return nombre; }
    public String getDescripcion()  { return descripcion; }
    public boolean estaFinalizada() { return finalizada; }
}
