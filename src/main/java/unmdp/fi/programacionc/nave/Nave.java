package unmdp.fi.programacionc.nave;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.motor.MotorWarp;

import java.util.ArrayList;

public abstract class Nave {
    protected int id;
    protected int combustible;
    protected int energia;
    protected int desgaste;
    protected ArrayList<Tripulante> Tripulantes;
    protected MotorWarp motor;

    protected final int topeCombustible=100;
    protected final int topeEnergia=100;
    protected final int topeDesgaste=100; // podria ser unsigned 0-100


    //CONSTRUCTOR
    public Nave(int combustible, int energia, int desgaste){
        this.combustible=combustible;
        this.energia=energia;
        this.desgaste=desgaste;
    }


    //METODOS
    /**
     *  pre -> carga>0
     */
    public void cargarCombustible(int carga){
        if (this.combustible==topeCombustible){}
            // no cargo -> excepcion check
        else{
            if (this.combustible+carga>topeCombustible)
                this.combustible=topeCombustible;
            else
                this.combustible+=carga;
        }
    }

    /**
     *  pre -> carga>0
     */
    public void cargarEnergia(int carga){
        if (this.energia==topeEnergia){}
        // no cargo -> excepcion check
        else{
            if (this.energia+carga>topeEnergia)
                this.energia=topeEnergia;
            else
                this.energia+=carga;
        }
    }

    //cuando desgaste>=80 (llama Asistentecomando
    public void mantenimiento(){
        this.desgaste=0;
    }

    public void agregarTripulante(Tripulante trip){
        this.Tripulantes.add(trip);
    }

    public void consumirRecursos(int consumicion){ //??

    }

    //GETTERS
    public int getCombustible(Nave nave){
        return this.combustible;
    }

    public int getEnergia(Nave nave) {
        return this.energia;
    }

    public int getDesgaste(Nave nave){
        return this.desgaste;
}
