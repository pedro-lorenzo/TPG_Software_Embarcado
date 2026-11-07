package unmdp.fi.programacionc.aplicacion;

import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.nave.NaveFactory;
import unmdp.fi.programacionc.nave.TipoNave;
import unmdp.fi.programacionc.universo.Universo;

import java.util.ArrayList;
import java.util.List;

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