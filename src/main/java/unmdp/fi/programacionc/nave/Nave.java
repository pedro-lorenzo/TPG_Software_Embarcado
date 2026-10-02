package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.AsistenteComando;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tripulacion.Tripulante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nave: identidad, tipo, componentes y tripulacion.
 *
 * La nave es duena de sus componentes (composicion), pero NO da ordenes ni expone su estado:
 * toda consulta u orden sobre motor y recursos pasa por su asistente de comando (R2).
 * Por eso la nave no tiene metodos que reenvien al tanque o al motor.
 *
 * Las naves solo las crea la fabrica (constructores package-private), que arma los
 * componentes, crea el asistente que los opera y se los pasa a la nave.
 *
 * inv -> id > 0
 * inv -> tipo, tanque, motor y asistente != null
 */
public abstract class Nave {

    private final int id;
    private final String tipo;
    private final Tanque tanque;
    private final MotorWarp motor;
    private final AsistenteComando asistente;
    private final List<Tripulante> tripulantes = new ArrayList<>();

    /**
     * pre -> id > 0
     * pre -> tipo, tanque, motor y asistente != null
     * pre -> el asistente opera este mismo tanque y este mismo motor (lo garantiza la fabrica)
     */
    Nave(int id, String tipo, Tanque tanque, MotorWarp motor, AsistenteComando asistente) {
        assert id > 0 : "El id de la nave debe ser positivo";
        assert TipoNave.esValido(tipo) : "Tipo de nave invalido: " + tipo;
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
    public int getId()          { return id; }
    public String getTipo() { return tipo; }

    // ---- acceso al asistente: unica via para consultar u ordenar a la nave ----
    public AsistenteComando getAsistente() { return asistente; }

    // ---- tripulacion (pendiente: definir clase Tripulacion y su invariante) ----
    /**
     * pre -> tripulante != null
     */
    public void agregarTripulante(Tripulante t) {
        assert t != null : "El tripulante no puede ser nulo";
        tripulantes.add(t);
    }

    public List<Tripulante> getTripulantes() { return Collections.unmodifiableList(tripulantes); }
}