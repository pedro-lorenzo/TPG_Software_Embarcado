package unmdp.fi.programacionc.universo;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.nave.Nave;

/**
 * Universo / centro de control (R1). Registra las naves listas para operar y devuelve
 * cualquiera de ellas cuando se la piden. Es el punto de entrada para quien use el sistema.
 *
 * Solo sabe QUE naves existen: no las crea (eso es de NaveFactory) ni las opera
 * (eso es del asistente de cada nave). Guarda el tipo abstracto Nave, asi que no depende
 * de como se construyen ni de que subclase concreta son.
 *
 * Usa un arreglo de capacidad fija (sin colecciones) y un contador de naves cargadas.
 *
 * inv -> 0 <= cantidad <= CAPACIDAD
 * inv -> naves[0 .. cantidad-1] != null y sin ids repetidos
 */
public class Universo {

    public static final int CAPACIDAD = 10;

    private final Nave[] naves = new Nave[CAPACIDAD];
    private int cantidad = 0;

    /**
     * pre -> nave != null
     * pre -> no hay otra nave registrada con el mismo id
     * post -> la nave queda registrada y se puede pedir con obtener(id)
     * @throws OperacionInvalidaException si el universo ya esta lleno (no se modifica nada)
     */
    public void registrar(Nave nave) {
        assert nave != null : "La nave a registrar no puede ser nula";
        assert buscar(nave.getId()) == null : "Ya hay una nave registrada con el id " + nave.getId();
        if (this.cantidad == CAPACIDAD) {
            throw new OperacionInvalidaException("El universo no tiene lugar para mas naves (maximo " + CAPACIDAD + ")");
        }
        this.naves[this.cantidad] = nave;
        this.cantidad++;
    }

    /**
     * post -> devuelve la nave registrada con ese id
     * @throws OperacionInvalidaException si no hay ninguna nave con ese id
     */
    public Nave obtener(int id) {
        Nave nave = buscar(id);
        if (nave == null) {
            throw new OperacionInvalidaException("No existe una nave registrada con el id " + id);
        }
        return nave;
    }

    public int cantidadNaves() {
        return this.cantidad;
    }

    // Busqueda lineal: devuelve null si no esta (uso interno, el cliente usa obtener)
    private Nave buscar(int id) {
        for (int i = 0; i < this.cantidad; i++) {
            if (this.naves[i].getId() == id) {
                return this.naves[i];
            }
        }
        return null;
    }
}