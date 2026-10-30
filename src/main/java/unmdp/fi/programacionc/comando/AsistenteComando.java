package unmdp.fi.programacionc.comando;

import unmdp.fi.programacionc.bitacora.Bitacora;
import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tablero.Coordenada;
import unmdp.fi.programacionc.tablero.Combatiente;
import unmdp.fi.programacionc.tablero.NaveEnemiga;
import unmdp.fi.programacionc.tablero.ResultadoBatalla;

import java.util.List;

/**
 * Asistente de Comando (E1-03): implementacion de Asistente. Opera UNA sola nave:
 * toda consulta u orden a la nave pasa por aca.
 *
 * Coordina, no implementa: las reglas de recursos son de Tanque y las transiciones del motor
 * son de los estados (patron State). El asistente solo:
 *   1. delega la orden en el componente que corresponde,
 *   2. registra en su bitacora que cambio (valor antes -> despues),
 *   3. si el componente rechaza la orden, registra el error y lo vuelve a lanzar
 *      (throw e) para que quien dio la orden (Main, mision o futura pantalla) se entere.
 *
 * Al implementar Asistente tambien es un OperadorMision: las misiones solo ven esa parte.
 * El resto de las ordenes (cargas, mantenimiento, enfriamiento) solo las ve quien opera la nave.
 *
 * inv -> tanque != null, motor != null, bitacora != null
 */
public class AsistenteComando implements Asistente {

    /** Combustible que se obtiene al canjear una gema. */
    public static final int COMBUSTIBLE_POR_GEMA = 5;

    private final Tanque tanque;
    private final MotorWarp motor;
    private final Bitacora bitacora;
    private Combatiente combatiente;
    private Coordenada posicion;           // null hasta que se ubica a la nave en el tablero
    private int gemas = 0;
    private boolean destruida = false;
    private String motivoDestruccion;

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

    /** Una nave destruida nunca esta disponible. */
    @Override public boolean estaDisponible() { return !destruida && motor.estaDisponible(); }
    @Override public String getEstadoMotor()  { return motor.getEstadoActual(); }
    @Override public int getCombustible()     { return tanque.getCombustible(); }
    @Override public int getEnergia()         { return tanque.getEnergia(); }
    @Override public int getDesgaste()        { return tanque.getDesgaste(); }

    @Override public boolean necesitaMantenimiento()    { return tanque.necesitaMantenimiento(); }

    /** Eventos en orden temporal. La lista no se puede modificar y los eventos son inmutables. */
    @Override
    public List<EventoBitacora> getEventos()  { return bitacora.getEventos(); }

    /**
     * Lo llama la fabrica una sola vez, al armar la nave.
     * pre -> combatiente != null
     * pre -> todavia no tiene uno asignado
     */
    public void asignarCombatiente(Combatiente combatiente) {
        assert combatiente != null : "El combatiente no puede ser nulo";
        assert this.combatiente == null : "El asistente ya tiene un combatiente asignado";
        this.combatiente = combatiente;
    }

    /**
     * pre -> la nave ya tiene una posicion asignada
     */
    @Override
    public Coordenada getPosicion() {
        assert posicion != null : "La nave todavia no fue ubicada en el tablero";
        return posicion;
    }

    @Override public boolean estaDestruida() { return destruida; }
    public String getMotivoDestruccion()      { return motivoDestruccion; }
    @Override public int getGemas()           { return gemas; }

    /**
     * pre -> la fabrica ya asigno el combatiente
     * pre -> enemiga != null
     */
    @Override
    public ResultadoBatalla enfrentar(NaveEnemiga enemiga) {
        assert combatiente != null : "El asistente no tiene combatiente asignado";
        assert enemiga != null : "La nave enemiga no puede ser nula";
        return combatiente.enfrentar(enemiga);
    }

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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public void realizarMantenimiento() {
        String antes = estadoRecursos();
        tanque.realizarMantenimiento();
        registrarCambioRecursos("realizar mantenimiento", antes);
    }

    // ===================== NAVEGACION, GEMAS Y DESTRUCCION =====================

    /**
     * La primera llamada ubica a la nave; las siguientes la mueven. No consume recursos:
     * el costo del viaje lo descuenta la mision.
     * pre -> destino != null
     * pre -> la nave no esta destruida
     * post -> getPosicion() equals destino
     */
    @Override
    public void moverA(Coordenada destino) {
        assert destino != null : "El destino no puede ser nulo";
        assert !destruida : "Una nave destruida no se mueve";
        String antes = posicion == null ? "sin ubicar" : posicion.toString();
        posicion = destino;
        bitacora.registrar(TipoEvento.SISTEMA, "Posicion: " + antes + " -> " + destino);
    }

    /**
     * pre -> cantidad > 0
     * post -> getGemas() aumenta en cantidad
     */
    @Override
    public void agregarGemas(int cantidad) {
        assert cantidad > 0 : "La cantidad de gemas debe ser positiva";
        gemas += cantidad;
        bitacora.registrar(TipoEvento.RECURSOS, "Gemas recolectadas: +" + cantidad + " (total " + gemas + ")");
    }

    /**
     * Cierre de la mision: las gemas pasan a ser combustible. Lo que no entra en el tanque se pierde.
     * post -> getGemas() == 0
     */
    @Override
    public void canjearGemas() {
        int canjeadas = gemas;
        int equivalente = canjeadas * COMBUSTIBLE_POR_GEMA;
        int cargable = Math.min(equivalente, Tanque.TOPE_COMBUSTIBLE - tanque.getCombustible());
        gemas = 0;
        if (cargable > 0) {
            cargarCombustible(cargable);
        }
        bitacora.registrar(TipoEvento.RECURSOS, "Canje de " + canjeadas + " gemas: +" + cargable
                + " de combustible" + (equivalente > cargable ? " (" + (equivalente - cargable)
                + " no entraron en el tanque)" : ""));
    }

    // ---- NavePropia: lo que los elementos del tablero le hacen a la nave ----

    /**
     * Descuenta energia sin pasarse de la que hay. Si queda en 0, la nave queda destruida.
     * pre -> energia > 0
     */
    @Override
    public void recibirImpactoDeEnergia(int energia) {
        assert energia > 0 : "El impacto debe ser positivo";
        int descontada = Math.min(energia, tanque.getEnergia());
        if (descontada > 0) {
            consumir(0, descontada, 0);
        }
        if (tanque.getEnergia() == 0) {
            destruir("sin energia tras el impacto");
        }
    }

    /**
     * Recarga hasta el tope de cada tanque, sin pasarse.
     * pre -> combustible >= 0 y energia >= 0
     */
    @Override
    public void recargar(int combustible, int energia) {
        assert combustible >= 0 && energia >= 0 : "Las recargas no pueden ser negativas";
        int c = Math.min(combustible, Tanque.TOPE_COMBUSTIBLE - tanque.getCombustible());
        int e = Math.min(energia, Tanque.TOPE_ENERGIA - tanque.getEnergia());
        if (c > 0) {
            cargarCombustible(c);
        }
        if (e > 0) {
            cargarEnergia(e);
        }
    }

    /**
     * Si la nave ya estaba destruida no hace nada (un mismo turno puede destruirla dos veces).
     * pre -> motivo no nulo ni vacio
     * post -> estaDestruida() == true
     */
    @Override
    public void destruir(String motivo) {
        assert motivo != null && !motivo.trim().isEmpty() : "El motivo no puede ser nulo ni vacio";
        if (destruida) {
            return;
        }
        destruida = true;
        motivoDestruccion = motivo;
        bitacora.registrar(TipoEvento.ERROR, "Nave destruida: " + motivo);
    }

    // ===================== REGISTRO =====================

    /**
     * pre -> tipo valido, descripcion no nula ni vacia (lo verifica Bitacora)
     */
    @Override
    public void registrarEvento(TipoEvento tipo, String descripcion) {
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