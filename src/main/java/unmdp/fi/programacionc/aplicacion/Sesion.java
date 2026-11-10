package unmdp.fi.programacionc.aplicacion;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.nave.NaveFactory;
import unmdp.fi.programacionc.nave.TipoNave;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.universo.Universo;
import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Tablero;
import unmdp.fi.programacionc.mision.Mision;
import unmdp.fi.programacionc.mision.MisionIntercepcion;
import unmdp.fi.programacionc.mision.MisionRecoleccion;
import unmdp.fi.programacionc.mision.MisionRetornoSeguro;
import unmdp.fi.programacionc.tablero.Asteroide;
import unmdp.fi.programacionc.tablero.ElementoDelUniverso;
import unmdp.fi.programacionc.tablero.EstacionDeCarga;
import unmdp.fi.programacionc.tablero.NaveEnemigaCarguero;
import unmdp.fi.programacionc.tablero.NaveEnemigaCombate;
import unmdp.fi.programacionc.tablero.NaveEnemigaExploradora;
import java.util.function.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo de la aplicacion (o "coordinador", segun el apunte de MVC de la catedra):
 * guarda el estado de la sesion de trabajo del operador y avisa a sus observadores
 * cuando cambia. Une el modelo del dominio (Universo, Nave) con las pantallas sin conocerlas.
 *
 * inv -> usuario no nulo ni vacio, universo != null
 */
public class Sesion {

    private final String usuario;
    private final Universo universo;
    private final List<ObservadorSesion> observadores = new ArrayList<>();
    private Nave naveActual;   // null hasta que el operador crea una
    private int proximoIdTripulante = 1;
    private final Map<Integer, Integer> consejosDelPeriodo = new HashMap<>();   // id del consejero -> consejos
    // ---- universo de la nave actual (uno por nave) y mision en curso ----
    public static final String MISION_INTERCEPCION = "M-01";
    public static final String MISION_RECOLECCION = "M-02";
    public static final String MISION_RETORNO = "M-03";

    private static final int LADO_TABLERO = 20;
    private static final int CANTIDAD_ASTEROIDES = 15;
    private static final int CANTIDAD_ESTACIONES = 4;
    private static final int ENEMIGAS_POR_TIPO = 2;

    private Tablero tablero;                  // null hasta que el operador crea una nave
    private Destino destino;
    private volatile boolean misionEnCurso = false;
    // Quien muestra la pantalla decide en que hilo se notifica (Swing: SwingUtilities::invokeLater)
    private Consumer<Runnable> despachador = Runnable::run;
    /**
     * pre -> usuario no nulo ni vacio (ya lo valido el login)
     * pre -> universo != null
     */
    public Sesion(String usuario, Universo universo) {
        assert usuario != null && !usuario.trim().isEmpty() : "La sesion necesita un usuario";
        assert universo != null : "La sesion necesita un universo";
        this.usuario = usuario;
        this.universo = universo;
    }

    // ===================== CONSULTAS =====================

    public String getUsuario() { return usuario; }

    public boolean tieneNave() { return naveActual != null; }

    public boolean hayMisionEnCurso() { return misionEnCurso; }

    /** El tablero de la nave actual, para que la vista lo dibuje (leerlo dentro de synchronized (tablero)). */
    public Tablero getTablero() {
        assert tablero != null : "Todavia no se creo ninguna nave";
        return tablero;
    }

    /**
     * Define en que hilo se avisa a la pantalla. Por defecto se avisa en el mismo hilo de quien notifica.
     * pre -> despachador != null
     */
    public void setDespachador(Consumer<Runnable> despachador) {
        assert despachador != null : "El despachador no puede ser nulo";
        this.despachador = despachador;
    }
    /**
     * pre -> tieneNave()
     */
    public Nave getNaveActual() {
        assert naveActual != null : "Todavia no se creo ninguna nave";
        return naveActual;
    }

    // ===================== OPERACIONES =====================

    /**
     * Crea una nave del tipo pedido, la registra en el universo y pasa a ser la nave en uso.
     * pre -> tipo != null
     * post -> getNaveActual() es la nave nueva; los observadores fueron avisados
     * @throws unmdp.fi.programacionc.excepciones.OperacionInvalidaException si el universo esta lleno
     */
    public Nave crearNave(TipoNave tipo) {
        assert tipo != null : "Hay que indicar el tipo de nave";
        if (misionEnCurso) {
            throw new OperacionInvalidaException("No se puede crear otra nave mientras hay una mision en curso");
        }
        Nave nave = NaveFactory.crear(tipo);
        universo.registrar(nave);
        naveActual = nave;
        crearUniversoDeLaNave();
        notificar();
        return nave;
    }

    /**
     * Da de alta un tripulante y lo asigna a la nave actual. La sesion le asigna el id.
     * Los datos vienen de lo que escribio el operador, por eso se validan con excepciones.
     * pre -> cargo != null y origen != null
     * post -> el tripulante queda en la nave actual; los observadores fueron avisados
     * @throws OperacionInvalidaException si no hay nave, si el nombre esta vacio, si la antiguedad
     *         es negativa o si la nave lo rechaza (por ejemplo, un segundo capitan)
     */
    public Tripulante agregarTripulante(String nombre, Cargo cargo, Origen origen, int antiguedad) {
        assert cargo != null && origen != null : "Cargo y origen son obligatorios";
        if (!tieneNave()) {
            throw new OperacionInvalidaException("Primero hay que crear una nave");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new OperacionInvalidaException("El tripulante necesita un nombre");
        }
        if (antiguedad < 0) {
            throw new OperacionInvalidaException("La antiguedad no puede ser negativa");
        }
        Tripulante tripulante = new Tripulante(proximoIdTripulante, nombre.trim(), cargo, origen, antiguedad);
        naveActual.agregarTripulante(tripulante);   // si la nave lo rechaza, no se consume el id
        proximoIdTripulante++;
        notificar();
        return tripulante;
    }

    /**
     * Anota un consejo del consejero en el periodo actual (suma 2 PG a su haber).
     * pre -> tripulante != null
     * post -> getConsejos(tripulante) aumenta en 1; los observadores fueron avisados
     * @throws OperacionInvalidaException si el tripulante no es consejero
     */
    public void registrarConsejo(Tripulante tripulante) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        if (tripulante.getCargo() != Cargo.CONSEJERO) {
            throw new OperacionInvalidaException("Solo los consejeros registran consejos");
        }
        consejosDelPeriodo.merge(tripulante.getId(), 1, Integer::sum);
        notificar();
    }

    /** Consejos registrados en el periodo actual (0 si no registro ninguno). */
    public int getConsejos(Tripulante tripulante) {
        return consejosDelPeriodo.getOrDefault(tripulante.getId(), 0);
    }

    /**
     * Lanza una mision en un hilo aparte, para que la ventana no se congele mientras la nave viaja.
     * Al terminar cada turno del tablero y al finalizar la mision se avisa a la pantalla.
     *
     * pre -> codigo es MISION_INTERCEPCION, MISION_RECOLECCION o MISION_RETORNO
     * post -> la mision queda en curso hasta que termina (en cualquier resultado)
     * @throws OperacionInvalidaException si no hay nave, la tripulacion esta incompleta
     *         o ya hay una mision en curso
     */
    public void iniciarMision(String codigo) {
        if (!tieneNave()) {
            throw new OperacionInvalidaException("Primero hay que crear una nave");
        }
        if (misionEnCurso) {
            throw new OperacionInvalidaException("Ya hay una mision en curso");
        }
        if (!naveActual.tieneTripulacionValida()) {
            throw new OperacionInvalidaException("La tripulacion esta incompleta");
        }
        Mision mision = crearMision(codigo);
        misionEnCurso = true;
        notificar();

        Thread hilo = new Thread(() -> {
            try {
                mision.ejecutarCiclo();
            } finally {
                misionEnCurso = false;
                despachador.accept(this::notificar);
            }
        });
        hilo.start();
    }

    private Mision crearMision(String codigo) {
        Asistente asistente = naveActual.getAsistente();
        switch (codigo) {
            case MISION_INTERCEPCION:
                return new MisionIntercepcion(asistente, tablero, destino);
            case MISION_RECOLECCION:
                return new MisionRecoleccion(asistente, tablero, destino);
            case MISION_RETORNO:
                return new MisionRetornoSeguro(asistente, tablero, destino);
            default:
                throw new OperacionInvalidaException("Mision desconocida: " + codigo);
        }
    }

    /** Cada nave tiene su propio tablero, asi conserva su posicion entre misiones (M-03 vuelve a la base). */
    private void crearUniversoDeLaNave() {
        ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
        for (int i = 0; i < CANTIDAD_ASTEROIDES; i++) {
            elementos.add(new Asteroide());
        }
        for (int i = 0; i < CANTIDAD_ESTACIONES; i++) {
            elementos.add(new EstacionDeCarga());
        }
        for (int i = 0; i < ENEMIGAS_POR_TIPO; i++) {
            elementos.add(new NaveEnemigaExploradora());
            elementos.add(new NaveEnemigaCarguero());
            elementos.add(new NaveEnemigaCombate());
        }
        try {
            tablero = new Tablero(LADO_TABLERO, LADO_TABLERO, elementos);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo crear el tablero de la nave", e);
        }
        destino = new Destino(tablero);
        // Cada turno del tablero refresca la pantalla (se avisa desde el hilo de la mision)
        tablero.agregarObservador(() -> despachador.accept(this::notificar));
    }
    // ===================== OBSERVER =====================

    /**
     * pre -> observador != null
     */
    public void agregarObservador(ObservadorSesion observador) {
        assert observador != null : "El observador no puede ser nulo";
        observadores.add(observador);
    }

    /** Avisa a todos que algo cambio. Lo usan tambien quienes modifican la nave actual. */
    public void notificar() {
        for (ObservadorSesion observador : observadores) {
            observador.sesionActualizada();
        }
    }
}