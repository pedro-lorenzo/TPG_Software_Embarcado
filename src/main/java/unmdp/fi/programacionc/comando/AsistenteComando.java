package unmdp.fi.programacionc.comando;

import unmdp.fi.programacionc.bitacora.Bitacora;
import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tablero.Combatiente;
import unmdp.fi.programacionc.tablero.NaveEnemiga;
import unmdp.fi.programacionc.tablero.ResultadoBatalla;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.mision.InformeMision;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public static final int COMBUSTIBLE_POR_GEMA = 1;

    private final Tanque tanque;
    private final MotorWarp motor;
    private final Bitacora bitacora;
    private final List<Tripulante> tripulantes;
    private Combatiente combatiente;
    private int gemas = 0;
    private boolean destruida = false;
    private String motivoDestruccion;
    private final List<InformeMision> informes = new CopyOnWriteArrayList<>();

    /**
     * Lo crea la fabrica de naves, que le pasa los componentes de la nave que va a operar.
     * pre -> tanque != null
     * pre -> motor != null
     * pre -> tripulantes != null
     * post -> el asistente tiene una bitacora propia y vacia (salvo el evento de inicio)
     */
    public AsistenteComando(Tanque tanque, MotorWarp motor, List<Tripulante> tripulantes) {
        assert tanque != null : "El asistente necesita el tanque de la nave";
        assert motor != null : "El asistente necesita el motor de la nave";
        assert tripulantes != null : "El asistente necesita la tripulacion de la nave";
        this.tanque = tanque;
        this.motor = motor;
        this.tripulantes = tripulantes;
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

    @Override
    public List<InformeMision> getInformes() { return informes; }
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
    // Anotar estado y recursos antes, delegar en el motor (el estado actual decide la transicion)
    // y registrar el cambio (salto simple o warp) o el rechazo.

    /**
     * Salto simple: el estado del motor decide la transicion.
     * pre -> la nave no esta destruida
     * @return casilleros avanzados
     * @throws OperacionInvalidaException si no alcanza el combustible (queda registrado)
     */
    @Override
    public int saltar() {
        return ordenarSalto("saltar", false);
    }

    /**
     * Salto warp si el motor esta en Warp; en los demas estados el motor hace el salto simple.
     * pre -> la nave no esta destruida
     * @return casilleros avanzados
     * @throws OperacionInvalidaException si no alcanza el combustible (queda registrado)
     */
    @Override
    public int saltarWarp() {
        return ordenarSalto("saltar warp", true);
    }

    private int ordenarSalto(String accion, boolean warp) {
        assert !destruida : "Una nave destruida no salta";
        String estadoAntes = motor.getEstadoActual();
        String recursosAntes = estadoRecursos();
        int avanzados;
        try {
            avanzados = warp ? motor.saltarWarp() : motor.saltar();
        } catch (OperacionInvalidaException e) {
            registrarError(accion, e);
            throw e;
        }
        String estadoDespues = motor.getEstadoActual();
        String tipo = avanzados == 2 ? "salto warp" : "salto simple";
        bitacora.registrar(TipoEvento.MOTOR, accion + " (" + tipo + ", avanza " + avanzados + "): "
                + estadoAntes + " -> " + estadoDespues);
        if (warp && estadoAntes.equals("En warp") && avanzados == 1) {
            bitacora.registrar(TipoEvento.MOTOR, "Salto warp abortado por energia insuficiente: "
                    + "se hizo el salto simple y el motor vuelve a " + estadoDespues);
        }
        registrarCambioRecursos(accion, recursosAntes);
        revisarAgotamiento();
        return avanzados;
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
        revisarAgotamiento();
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
     * pre -> cantidad > 0
     * post -> getGemas() aumenta en cantidad
     */
    @Override
    public void agregarGemas(int cantidad) {
        assert cantidad > 0 : "La cantidad de gemas debe ser positiva";
        gemas += cantidad;
        bitacora.registrar(TipoEvento.RECURSOS, "Gemas recolectadas: +" + cantidad + " (total " + gemas + ")");
    }

    @Override
    public void recargarTotal() {
        String antes = estadoRecursos();
        tanque.recargarHastaTope(Tanque.TOPE_COMBUSTIBLE, Tanque.TOPE_ENERGIA);
        registrarCambioRecursos("recarga total de combustible y energia", antes);
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
     * El tanque descuenta la energia sin pasarse de la que hay. Si la nave queda agotada, se destruye.
     * pre -> energia > 0
     */
    @Override
    public void recibirImpactoDeEnergia(int energia) {
        assert energia > 0 : "El impacto debe ser positivo";
        String antes = estadoRecursos();
        tanque.recibirDanio(energia);
        registrarCambioRecursos("recibir impacto de " + energia + " de energia", antes);
        revisarAgotamiento();
    }

    /**
     * Recarga hasta el tope de cada tanque, sin pasarse.
     * pre -> combustible >= 0 y energia >= 0
     */
    @Override
    public void recargar(int combustible, int energia) {
        assert combustible >= 0 && energia >= 0 : "Las recargas no pueden ser negativas";
        String antes = estadoRecursos();
        tanque.recargarHastaTope(combustible, energia);
        registrarCambioRecursos("recargar " + combustible + " de combustible y " + energia + " de energia", antes);
        revisarAgotamiento();
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
        int muertos = 0;
        for (Tripulante t : tripulantes) {
            if (!t.estaFallecido()) {
                t.marcarFallecido();
                muertos++;
            }
        }
        bitacora.registrar(TipoEvento.ERROR, "Nave destruida: " + motivo + ". Tripulantes fallecidos: " + muertos);
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

    private void registrarCambioRecursos(String accion, String recursosAntes) {
        bitacora.registrar(TipoEvento.RECURSOS, accion + ". Antes: " + recursosAntes
                + ". Despues: " + estadoRecursos());
    }

    /** La nave se destruye con desgaste maximo, energia 0 o combustible 0 (lo decide Tanque). */
    private void revisarAgotamiento() {
        if (!destruida && tanque.estaAgotado()) {
            destruir("recursos agotados (" + estadoRecursos() + ")");
        }
    }

    private String estadoRecursos() {
        return "combustible " + tanque.getCombustible() + ", energia " + tanque.getEnergia()
                + ", desgaste " + tanque.getDesgaste();
    }
    @Override
    public void registrarInforme(InformeMision informe) {
        assert informe != null : "El informe no puede ser nulo";
        informes.add(informe);
    }
}