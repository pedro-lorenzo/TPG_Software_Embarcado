package unmdp.fi.programacionc.mision;
import unmdp.fi.programacionc.nave.Nave;

public abstract class Mision {
    protected String id; //puede ser int idk
    protected String nombre;
    protected String descripcion;

    //METODOS

    public Mision(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // ACA VAMOS A USAR EL PATRON TEMPLATE!!
    public final InformeMision ejecutarCiclo(Nave nave){
        preparar(nave);
        ejecutar(nave);
        evaluar(nave);
        cerrar(nave);
    }

    // estos dos se desarrollan override en cada mision particular
    public abstract void ejecutar(Nave nave);
    public abstract void evaluar(Nave nave);
    //evaluar puede ser boolean o devolver un resultado...

    // estos son los comunes
    public void preparar(Nave nave){
        //verificar recursos (existencias)
        if (nave.getCombustible() < 4) {
            //throw new EXCEPCION "combustible insuficiente para iniciar mision "
        }
        if (nave.getDesgaste() + 4 > 100) {
            //throw new  EXCEPCION "nave requiere mantenimiento previo "
        }
    }

    public void cerrar(Nave nave){
        //registro en bitacora y genera InformeMision??
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

}
