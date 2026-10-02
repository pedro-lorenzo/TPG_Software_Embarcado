package unmdp.fi.programacionc.recursos;

import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;

// inv -> 0 <= combustible <= TOPE_COMBUSTIBLE
// inv -> 0 <= energia <= TOPE_ENERGIA
// inv -> 0 <= desgaste <= TOPE_DESGASTE
public class Tanque {

    public static final int TOPE_COMBUSTIBLE = 100;
    public static final int TOPE_ENERGIA = 100;
    public static final int TOPE_DESGASTE = 100;
    public static final int UMBRAL_MANTENIMIENTO = 80;

    private int combustible;
    private int energia;
    private int desgaste;

    /**
     * pre -> 0 <= combustible <= TOPE_COMBUSTIBLE
     * pre -> 0 <= energia <= TOPE_ENERGIA
     * post -> desgaste == 0 y el tanque cumple la invariante
     */
    public Tanque(int combustible, int energia) {
        assert combustible >= 0 && combustible <= TOPE_COMBUSTIBLE : "Combustible inicial fuera de rango: " + combustible;
        assert energia >= 0 && energia <= TOPE_ENERGIA : "Energia inicial fuera de rango: " + energia;
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = 0;
        assert verificarInvariante();
    }

    /**
     * pre -> carga > 0
     * post -> combustible = combustible anterior + carga
     * @throws OperacionInvalidaException si excede la capacidad (no se modifica nada)
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
     * @throws OperacionInvalidaException si excede la capacidad (no se modifica nada)
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
     * Atomico: si algo no alcanza, no se modifica ningun campo.
     * pre -> combustible, energia y desgasteAdicional >= 0
     * post -> cada recurso queda descontado (el desgaste incrementado) en el valor indicado
     * @throws OperacionInvalidaException si algun recurso no alcanza o el desgaste quedaria fuera de rango
     */
    public void consumir(int combustible, int energia, int desgasteAdicional) {
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

    private boolean verificarInvariante() {
        return this.combustible >= 0 && this.combustible <= TOPE_COMBUSTIBLE
                && this.energia >= 0 && this.energia <= TOPE_ENERGIA
                && this.desgaste >= 0 && this.desgaste <= TOPE_DESGASTE;
    }

    public boolean puedeConsumir(int combustible, int energia, int desgasteAdicional) {
        return this.combustible >= combustible
                && this.energia >= energia
                && this.desgaste + desgasteAdicional <= TOPE_DESGASTE;
    }

    public int getCombustible() { return this.combustible; }
    public int getEnergia()     { return this.energia; }
    public int getDesgaste()    { return this.desgaste; }
}
