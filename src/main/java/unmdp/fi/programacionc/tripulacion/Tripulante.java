package unmdp.fi.programacionc.tripulacion;

public class Tripulante {
    protected int id;
    protected String nombre;
    protected String cargo; //4 cargos
    protected String origen; // 3 origenes
    protected int antiguedad;


    // constructor
    // no se si validar con excepciones o con pre y post los datos
    public Tripulante(int id, String nombre, String cargo, String origen, int antiguedad) {
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
