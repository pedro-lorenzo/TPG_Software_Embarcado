package unmdp.fi.programacionc.comando;

import unmdp.fi.programacionc.bitacora.Bitacora;
import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.mision.OperadorMision;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.nave.Tanque;

import java.util.List;

/**
 * Asistente de Comando (E1-03). Opera UNA sola nave: toda consulta u orden a la nave pasa por aca.
 *
 * Coordina, no implementa: las reglas de recursos son de Tanque y las transiciones del motor
 * son de los estados (patron State). El asistente solo:
 *   1. delega la orden en el componente que corresponde,
 *   2. registra en su bitacora que cambio (valor antes -> despues),
 *   3. si el componente rechaza la orden, registra el error y lo vuelve a lanzar
 *      (throw e) para que quien dio la orden (Main, mision o futura pantalla) se entere.
 *
 * Implementa OperadorMision: lo que las misiones pueden ver. El resto de las ordenes
 * (cargas, mantenimiento, enfriamiento) solo las ve quien tiene al AsistenteComando.
 *
 * inv -> tanque != null, motor != null, bitacora != null
 */
public class AsistenteComando implements OperadorMision {

    private final Tanque tanque;
    private final MotorWarp motor;
    private final Bitacora bitacora;

    /**
     * Lo crea la fabrica de naves, que le pasa los componentes de la nave que va a operar.
     * pre -> tanque != null
     * pre -> motor != null
     * post -> el asistente tiene una bitacora propia y vacia (salvo el evento de inicio)
     */
    public AsistenteComando(Tanque tanque, MotorWarp motor) {
        assert tanque != null : "El asistente necesita el tanque de la nave";
        assert motor != null : "El asistente necesita el motor de la nave";
        this.tanque = tanque;
        this.motor = motor;
        this.bitacora = new Bitacora();
        this.bitacora.registrar(TipoEvento.SISTEMA, "Asistente de comando iniciado. " + estadoRecursos()
                + ", motor " + motor.getEstadoActual());
    }

    // ===================== CONSULTAS =====================

    @Override public boolean estaDisponible() { return motor.estaDisponible(); }
    @Override public String getEstadoMotor()  { return motor.getEstadoActual(); }
    @Override public int getCombustible()     { return tanque.getCombustible(); }
    @Override public int getEnergia()         { return tanque.getEnergia(); }
    @Override public int getDesgaste()        { return tanque.getDesgaste(); }

    @Override public boolean necesitaMantenimiento()    { return tanque.necesitaMantenimiento(); }

    /** Eventos en orden temporal. La lista no se puede modificar y los eventos son inmutables. */
    public List<EventoBitacora> getEventos()  { return bitacora.getEventos(); }

    // ===================== ORDENES AL MOTOR =====================
    // Todas siguen los mismos pasos: anotar el estado antes, delegar en el motor
    // (el estado actual decide si la transicion es valida) y registrar el cambio o el error.

    /**
     * Disponible -> Preparando salto.
     * @throws OperacionInvalidaException si el estado actual no lo permite (queda registrado)
     */
    @Override
    public void prepararSalto() {
        String accion = "preparar salto";
        String antes = motor.getEstadoActual();
        try {
            motor.prepararSalto();
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioMotor(accion, antes);
    }

    /**
     * Preparando salto -> En warp.
     * @throws OperacionInvalidaException si el estado actual no lo permite (queda registrado)
     */
    @Override
    public void saltar() {
        String accion = "saltar";
        String antes = motor.getEstadoActual();
        try {
            motor.saltar();
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioMotor(accion, antes);
    }

    /**
     * En warp -> Disponible (aclaracion R3: por ahora no se modela el paso del tiempo).
     * @throws OperacionInvalidaException si el estado actual no lo permite (queda registrado)
     */
    @Override
    public void finalizarSalto() {
        String accion = "finalizar salto";
        String antes = motor.getEstadoActual();
        try {
            motor.finalizarSalto();
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioMotor(accion, antes);
    }

    /**
     * En warp -> Enfriamiento.
     * @throws OperacionInvalidaException si el estado actual no lo permite (queda registrado)
     */
    public void enfriar() {
        String accion = "enfriar";
        String antes = motor.getEstadoActual();
        try {
            motor.enfriar();
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioMotor(accion, antes);
    }

    /**
     * Enfriamiento -> Disponible.
     * @throws OperacionInvalidaException si el estado actual no lo permite (queda registrado)
     */
    public void completarEnfriamiento() {
        String accion = "completar enfriamiento";
        String antes = motor.getEstadoActual();
        try {
            motor.completarEnfriamiento();
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioMotor(accion, antes);
    }

    // ===================== ORDENES SOBRE RECURSOS =====================
    // Mismos pasos que las del motor: anotar los recursos antes, delegar en el tanque
    // (que verifica limites y no modifica nada si la operacion no es valida) y registrar.

    /**
     * pre -> combustible, energia y desgaste >= 0 (lo verifica Tanque)
     * @throws OperacionInvalidaException si algun recurso no alcanza (la nave no se modifica)
     */
    @Override
    public void consumir(int combustible, int energia, int desgaste) {
        String accion = "consumir " + combustible + " de combustible, " + energia + " de energia y "
                + desgaste + " de desgaste";
        String antes = estadoRecursos();
        try {
            tanque.consumir(combustible, energia, desgaste);
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioRecursos(accion, antes);
    }

    /**
     * pre -> carga > 0 (lo verifica Tanque)
     * @throws OperacionInvalidaException si excede la capacidad (la nave no se modifica)
     */
    public void cargarCombustible(int carga) {
        String accion = "cargar " + carga + " de combustible";
        String antes = estadoRecursos();
        try {
            tanque.cargarCombustible(carga);
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioRecursos(accion, antes);
    }

    /**
     * pre -> carga > 0 (lo verifica Tanque)
     * @throws OperacionInvalidaException si excede la capacidad (la nave no se modifica)
     */
    public void cargarEnergia(int carga) {
        String accion = "cargar " + carga + " de energia";
        String antes = estadoRecursos();
        try {
            tanque.cargarEnergia(carga);
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        registrarCambioRecursos(accion, antes);
    }

    /**
     * post -> desgaste == 0
     * (Tanque no rechaza esta operacion, por eso no hace falta try/catch)
     */
    public void realizarMantenimiento() {
        String antes = estadoRecursos();
        tanque.realizarMantenimiento();
        registrarCambioRecursos("realizar mantenimiento", antes);
    }

    // ===================== REGISTRO =====================

    /**
     * pre -> tipo valido, descripcion no nula ni vacia (lo verifica Bitacora)
     */
    @Override
    public void registrarEvento(String tipo, String descripcion) {
        bitacora.registrar(tipo, descripcion);
    }

    // ===================== AUXILIARES PRIVADOS =====================

    private void registrarError(String accion, OperacionInvalidaException e) {
        bitacora.registrar(TipoEvento.ERROR, "Orden rechazada (" + accion + "): " + e.getMessage());
    }

    private void registrarCambioMotor(String accion, String estadoAntes) {
        bitacora.registrar(TipoEvento.MOTOR, accion + ": " + estadoAntes + " -> " + motor.getEstadoActual());
    }

    private void registrarCambioRecursos(String accion, String recursosAntes) {
        bitacora.registrar(TipoEvento.RECURSOS, accion + ". Antes: " + recursosAntes
                + ". Despues: " + estadoRecursos());
    }

    private String estadoRecursos() {
        return "combustible " + tanque.getCombustible() + ", energia " + tanque.getEnergia()
                + ", desgaste " + tanque.getDesgaste();
    }
}