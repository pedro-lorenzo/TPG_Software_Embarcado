package unmdp.fi.programacionc.tripulacion;

public class Tripulante {
    protected int id;
    protected String nombre;
    protected String cargo; //4 cargos
    protected String origen; // 3 origenes
    protected int antiguedad;

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
