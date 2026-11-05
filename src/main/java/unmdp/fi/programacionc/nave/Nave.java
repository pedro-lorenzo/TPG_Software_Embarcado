package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.tablero.Combatiente;

import java.util.Collections;
import java.util.List;

/**
 * Nave (E1-01, Aclaracion R2): identidad, tipo, componentes y tripulacion.
 *
 * Es duena de sus componentes (composicion: el tanque, el motor y el asistente se crean con ella),
 * pero no da ordenes ni expone su estado: toda consulta u orden pasa por su asistente de comando,
 * que opera este mismo tanque y este mismo motor. Por eso no tiene metodos que reenvien a ellos.
 *
 * Solo la crea NaveFactory (constructores package-private).
 *
 * inv -> id > 0
 * inv -> tipo != null
 * inv -> tanque, motor y asistente != null
 * inv -> los ids de los tripulantes no se repiten y hay a lo sumo un capitan
 */
public abstract class Nave implements Combatiente {

    private final int id;
    private final TipoNave tipo;
    private final Tanque tanque;
    private final MotorWarp motor;
    private final Asistente asistente;
    private final List<Tripulante> tripulantes;

    /**
     * pre -> id > 0
     * pre -> tipo != null
     * pre -> tanque, motor y asistente != null
     * pre -> el asistente opera este mismo tanque y este mismo motor (lo garantiza la fabrica)
     */
    Nave(int id, TipoNave tipo, Tanque tanque, MotorWarp motor, Asistente asistente,  List<Tripulante> tripulantes) {
        assert id > 0 : "El id de la nave debe ser positivo";
        assert tipo != null : "El tipo de nave no puede ser nulo";
        assert tanque != null : "La nave necesita un tanque";
        assert motor != null : "La nave necesita un motor";
        assert asistente != null : "La nave necesita un asistente de comando";
        assert tripulantes != null : "La nave necesita su lista de tripulantes";
        this.id = id;
        this.tipo = tipo;
        this.tanque = tanque;
        this.motor = motor;
        this.asistente = asistente;
        this.tripulantes = tripulantes;
    }

    // ---- identidad ----
    public int getId()      { return id; }
    public TipoNave getTipo() { return tipo; }

    // ---- unica via para consultar u ordenar a la nave ----
    public Asistente getAsistente() { return asistente; }

    // ---- tripulacion ----

    /** Minimo de tripulantes que acompanan al capitan para que la tripulacion sea valida. */
    public static final int MINIMO_TRIPULANTES_SIN_CAPITAN = 4;

    /**
     * pre -> tripulante != null
     * post -> el tripulante queda agregado a la tripulacion
     * @throws OperacionInvalidaException si ya hay un tripulante con ese id o si es capitan
     *         y la nave ya tiene uno (la tripulacion no se modifica)
     */
    public void agregarTripulante(Tripulante tripulante) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        for (Tripulante existente : tripulantes) {
            if (existente.getId() == tripulante.getId()) {
                throw new OperacionInvalidaException("Ya existe un tripulante con id " + tripulante.getId());
            }
        }
        if (tripulante.getCargo() == Cargo.CAPITAN && cantidadDeCapitanes() > 0) {
            throw new OperacionInvalidaException("La nave ya tiene un capitan");
        }
        int antes = tripulantes.size();
        tripulantes.add(tripulante);
        assert tripulantes.size() == antes + 1 : "El tripulante no se agrego";
        assert cantidadDeCapitanes() <= 1 : "La nave no puede tener mas de un capitan";
    }

    /**
     * post -> no modifica la nave
     * @return true si hay exactamente 1 capitan y al menos MINIMO_TRIPULANTES_SIN_CAPITAN tripulantes mas
     */
    public boolean tieneTripulacionValida() {
        int capitanes = cantidadDeCapitanes();
        return capitanes == 1 && tripulantes.size() - capitanes >= MINIMO_TRIPULANTES_SIN_CAPITAN;
    }

    private int cantidadDeCapitanes() {
        int capitanes = 0;
        for (Tripulante t : tripulantes) {
            if (t.getCargo() == Cargo.CAPITAN) {
                capitanes++;
            }
        }
        return capitanes;
    }

    /** Tripulacion de la nave. La lista no se puede modificar. */
    public List<Tripulante> getTripulantes() { return Collections.unmodifiableList(tripulantes); }
}
