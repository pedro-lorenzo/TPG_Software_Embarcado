package unmdp.fi.programacionc.tripulacion;

/**
 * inv -> nombre != null
 * inv -> cargo y origen son valores admitidos
 * inv -> antiguedad >= 0
 * Inmutable: sus datos no cambian despues de creado.
 */

public class Tripulante {
    private final int id;
    private final String nombre;
    private final String cargo;    // 4 cargos
    private final String origen;   // 3 origenes
    private final int antiguedad;


    /**
     * pre -> nombre != null
     * pre -> Cargo.esValido(cargo) y Origen.esValido(origen)
     * pre -> antiguedad >= 0
     */

    public Tripulante(int id, String nombre, String cargo, String origen, int antiguedad) {
        assert nombre != null : "El nombre no puede ser nulo";
        assert Cargo.esValido(cargo) : "Cargo invalido: " + cargo;
        assert Origen.esValido(origen) : "Origen invalido: " + origen;
        assert antiguedad >= 0 : "La antiguedad no puede ser negativa";
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.origen = origen;
        this.antiguedad = antiguedad;
    }
    //GETTERS
    public int getId()         { return id; }
    public String getNombre()  { return nombre; }
    public String getCargo()   { return cargo; }
    public String getOrigen()  { return origen; }
    public int getAntiguedad() { return antiguedad; }





}
