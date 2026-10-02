package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Nave: identidad, tripulacion, motor y recursos. Las invariantes de recursos son de Tanque.
// Toda consulta u orden llega desde el asistente (motor y tanque no se exponen).
public abstract class Nave {

    private final int id;
    private final String tipo;
    private final List<Tripulante> tripulantes = new ArrayList<>();
    private final MotorWarp motor = new MotorWarp();
    private final Tanque tanque;

    /**
     * pre -> tipo != null
     * pre -> combustible y energia dentro de rango (lo verifica Tanque)
     */
    protected Nave(int id, String tipo, int combustible, int energia) {
        assert tipo != null : "El tipo de nave no puede ser nulo";
        this.id = id;
        this.tipo = tipo;
        this.tanque = new Tanque(combustible, energia);
    }

    // ---- recursos (delegan en Tanque) ----
    public void cargarCombustible(int carga) { tanque.cargarCombustible(carga); }
    public void cargarEnergia(int carga)     { tanque.cargarEnergia(carga); }
    public void consumirParaOperacion(int c, int e, int d) { tanque.consumir(c, e, d); }
    public boolean puedeConsumir(int c, int e, int d)      { return tanque.puedeConsumir(c, e, d); }
    public boolean necesitaMantenimiento()   { return tanque.necesitaMantenimiento(); }
    public void realizarMantenimiento()      { tanque.realizarMantenimiento(); }
    public int getCombustible() { return tanque.getCombustible(); }
    public int getEnergia()     { return tanque.getEnergia(); }
    public int getDesgaste()    { return tanque.getDesgaste(); }

    // ---- motor (delegan en MotorWarp) ----
    public void prepararSalto()  { motor.prepararSalto(); }
    public void saltar()         { motor.saltar(); }
    public void enfriar()        { motor.enfriar(); }
    public void finalizarSalto() { motor.finalizarSalto(); }
    public void completarEnfriamiento() { motor.completarEnfriamiento(); }
    public boolean estaDisponible() { return motor.estaDisponible(); }
    public String getEstadoMotor()  { return motor.getEstadoActual(); }

    // ---- tripulacion ----
    /**
     * pre -> tripulante != null
     */
    public void agregarTripulante(Tripulante t) {
        assert t != null : "El tripulante no puede ser nulo";
        tripulantes.add(t);
    }
    public List<Tripulante> getTripulantes() { return Collections.unmodifiableList(tripulantes); }

    public int getId()      { return id; }
    public String getTipo() { return tipo; }
}