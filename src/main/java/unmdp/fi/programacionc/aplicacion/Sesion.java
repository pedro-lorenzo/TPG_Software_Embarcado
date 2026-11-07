package unmdp.fi.programacionc.aplicacion;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.nave.NaveFactory;
import unmdp.fi.programacionc.nave.TipoNave;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.universo.Universo;

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
        Nave nave = NaveFactory.crear(tipo);
        universo.registrar(nave);
        naveActual = nave;
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