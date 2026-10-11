package unmdp.fi.programacionc.tripulacion;

/**
 * Tripulante de la nave (E1-04). Inmutable: sus datos no cambian despues de creado.
 *
 * inv -> nombre != null
 * inv -> cargo != null y origen != null
 * inv -> antiguedad >= 0
 */
public class Tripulante {

    private final int id;
    private final String nombre;
    private final Cargo cargo;
    private final Origen origen;
    private final int antiguedad;

    /**
     * Los datos llegan validados por quien crea al tripulante, por eso son precondiciones.
     *
     * pre -> nombre != null
     * pre -> cargo != null y origen != null
     * pre -> antiguedad >= 0
     */
    public Tripulante(int id, String nombre, Cargo cargo, Origen origen, int antiguedad) {
        assert nombre != null : "El nombre no puede ser nulo";
        assert cargo != null : "El cargo no puede ser nulo";
        assert origen != null : "El origen no puede ser nulo";
        assert antiguedad >= 0 : "La antiguedad no puede ser negativa";
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.origen = origen;
        this.antiguedad = antiguedad;
    }

    public int getId()         { return id; }
    public String getNombre()  { return nombre; }
    public Cargo getCargo()    { return cargo; }
    public Origen getOrigen()  { return origen; }
    public int getAntiguedad() { return antiguedad; }
}
