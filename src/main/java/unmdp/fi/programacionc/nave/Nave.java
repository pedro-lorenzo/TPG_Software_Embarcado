package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tripulacion.Tripulante;

import java.util.ArrayList;
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
 */
public abstract class Nave {

    private final int id;
    private final TipoNave tipo;
    private final Tanque tanque;
    private final MotorWarp motor;
    private final Asistente asistente;
    private final List<Tripulante> tripulantes = new ArrayList<>();

    /**
     * pre -> id > 0
     * pre -> tipo != null
     * pre -> tanque, motor y asistente != null
     * pre -> el asistente opera este mismo tanque y este mismo motor (lo garantiza la fabrica)
     */
    Nave(int id, TipoNave tipo, Tanque tanque, MotorWarp motor, Asistente asistente) {
        assert id > 0 : "El id de la nave debe ser positivo";
        assert tipo != null : "El tipo de nave no puede ser nulo";
        assert tanque != null : "La nave necesita un tanque";
        assert motor != null : "La nave necesita un motor";
        assert asistente != null : "La nave necesita un asistente de comando";
        this.id = id;
        this.tipo = tipo;
        this.tanque = tanque;
        this.motor = motor;
        this.asistente = asistente;
    }

    // ---- identidad ----
    public int getId()      { return id; }
    public TipoNave getTipo() { return tipo; }

    // ---- unica via para consultar u ordenar a la nave ----
    public Asistente getAsistente() { return asistente; }

    // ---- tripulacion ----

    /**
     * pre -> tripulante != null
     * post -> el tripulante queda agregado a la tripulacion
     */
    public void agregarTripulante(Tripulante tripulante) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        tripulantes.add(tripulante);
    }

    /** Tripulacion de la nave. La lista no se puede modificar. */
    public List<Tripulante> getTripulantes() { return Collections.unmodifiableList(tripulantes); }
}
