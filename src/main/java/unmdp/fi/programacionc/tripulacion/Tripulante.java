package unmdp.fi.programacionc.tripulacion;

public class Tripulante {
    protected int id;
    protected String nombre;
    protected String cargo; //4 cargos
    protected String origen; // 3 origenes
    protected int antiguedad;


    /**
     * Los datos llegan ya validados desde quien crea al tripulante, por eso son precondiciones.
     * pre -> nombre, cargo y origen != null
     * pre -> antiguedad >= 0
     */
    public Tripulante(int id, String nombre, String cargo, String origen, int antiguedad) {
        assert nombre != null && cargo != null && origen != null : "Nombre, cargo y origen no pueden ser nulos";
        assert antiguedad >= 0 : "La antiguedad no puede ser negativa";
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.origen = origen;
        this.antiguedad = antiguedad;
    }
    //GETTERS
    public int getAntiguedad(){
        return this.antiguedad;
    }

    public String getOrigen(){
        return this.origen;
    }

    public String getCargo(){
        return this.cargo;
    }






}
