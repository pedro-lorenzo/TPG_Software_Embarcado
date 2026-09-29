package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.bitacora.Bitacora;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.tripulacion.Tripulante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Manejo de errores:
//  - pre + assert: datos que el llamador ya conoce y debe validar antes de llamar
//    (argumentos nulos, negativos, configuracion inicial de las subclases).
//  - excepcion: condiciones que dependen del estado actual de la nave y que el llamador
//    no puede garantizar (capacidad excedida, recursos insuficientes).
//
// inv -> 0 <= combustible <= TOPE_COMBUSTIBLE
// inv -> 0 <= energia <= TOPE_ENERGIA
// inv -> 0 <= desgaste <= TOPE_DESGASTE
public abstract class Nave {

    public static final int TOPE_COMBUSTIBLE = 100;
    public static final int TOPE_ENERGIA = 100;
    public static final int TOPE_DESGASTE = 100;
    public static final int UMBRAL_MANTENIMIENTO = 80;

    protected final int id;
    protected final String tipo;
    protected int combustible;
    protected int energia;
    protected int desgaste;
    protected final List<Tripulante> tripulantes = new ArrayList<>();
    protected final MotorWarp motor = new MotorWarp();
    protected final Bitacora bitacora = new Bitacora();

    /**
     * pre -> tipo != null
     * pre -> 0 <= combustible <= TOPE_COMBUSTIBLE
     * pre -> 0 <= energia <= TOPE_ENERGIA
     * pre -> 0 <= desgaste <= TOPE_DESGASTE
     * post -> la nave cumple la invariante
     */
    protected Nave(int id, String tipo, int combustible, int energia, int desgaste) {
        assert tipo != null : "El tipo de nave no puede ser nulo";
        assert combustible >= 0 && combustible <= TOPE_COMBUSTIBLE : "Combustible inicial fuera de rango: " + combustible;
        assert energia >= 0 && energia <= TOPE_ENERGIA : "Energia inicial fuera de rango: " + energia;
        assert desgaste >= 0 && desgaste <= TOPE_DESGASTE : "Desgaste inicial fuera de rango: " + desgaste;
        this.id = id;
        this.tipo = tipo;
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        assert verificarInvariante();
    }

    /**
     * pre -> carga > 0
     * post -> combustible = combustible anterior + carga
     * @throws OperacionInvalidaException si la carga excede la capacidad actual (no se modifica nada)
     */
    public void cargarCombustible(int carga) {
        assert carga > 0 : "La carga de combustible debe ser positiva";
        if (this.combustible + carga > TOPE_COMBUSTIBLE) {
            throw new OperacionInvalidaException("La carga excede la capacidad maxima de combustible");
        }
        this.combustible += carga;
        assert verificarInvariante();
    }

    /**
     * pre -> carga > 0
     * post -> energia = energia anterior + carga
     * @throws OperacionInvalidaException si la carga excede la capacidad actual (no se modifica nada)
     */
    public void cargarEnergia(int carga) {
        assert carga > 0 : "La carga de energia debe ser positiva";
        if (this.energia + carga > TOPE_ENERGIA) {
            throw new OperacionInvalidaException("La carga excede la capacidad maxima de energia");
        }
        this.energia += carga;
        assert verificarInvariante();
    }

    /**
     * Aplica el consumo de una operacion (mision, avance, escaneo, etc) de forma atomica:
     * si algun recurso no alcanza, no se modifica ningun campo.
     * pre -> combustible, energia y desgasteAdicional >= 0
     * post -> cada recurso queda descontado (o el desgaste incrementado) en el valor indicado
     * @throws OperacionInvalidaException si algun recurso no alcanza o el desgaste quedaria fuera de rango
     */
    public void consumirParaOperacion(int combustible, int energia, int desgasteAdicional) {
        assert combustible >= 0 && energia >= 0 && desgasteAdicional >= 0 : "Los consumos no pueden ser negativos";
        if (this.combustible < combustible) {
            throw new OperacionInvalidaException("Combustible insuficiente para la operacion");
        }
        if (this.energia < energia) {
            throw new OperacionInvalidaException("Energia insuficiente para la operacion");
        }
        if (this.desgaste + desgasteAdicional > TOPE_DESGASTE) {
            throw new OperacionInvalidaException("La operacion dejaria el desgaste fuera de rango");
        }
        this.combustible -= combustible;
        this.energia -= energia;
        this.desgaste += desgasteAdicional;
        assert verificarInvariante();
    }

    public boolean necesitaMantenimiento() {
        return this.desgaste >= UMBRAL_MANTENIMIENTO;
    }

    /**
     * post -> desgaste == 0
     */
    public void realizarMantenimiento() {
        this.desgaste = 0;
        assert verificarInvariante();
    }

    /**
     * pre -> tripulante != null
     * post -> el tripulante queda agregado a la tripulacion
     */
    public void agregarTripulante(Tripulante tripulante) {
        assert tripulante != null : "El tripulante no puede ser nulo";
        this.tripulantes.add(tripulante);
    }

    private boolean verificarInvariante() {
        return this.combustible >= 0 && this.combustible <= TOPE_COMBUSTIBLE
                && this.energia >= 0 && this.energia <= TOPE_ENERGIA
                && this.desgaste >= 0 && this.desgaste <= TOPE_DESGASTE;
    }

    public List<Tripulante> getTripulantes() {
        return Collections.unmodifiableList(this.tripulantes);
    }

    public int getId() {
        return this.id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public int getCombustible() {
        return this.combustible;
    }

    public int getEnergia() {
        return this.energia;
    }

    public int getDesgaste() {
        return this.desgaste;
    }

    public MotorWarp getMotor() {
        return this.motor;
    }

    public Bitacora getBitacora() {
        return this.bitacora;
    }
}
