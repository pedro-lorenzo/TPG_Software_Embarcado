package unmdp.fi.programacionc.motor;

public class Warp implements EstadoMotor {

    @Override public void enfriar(MotorWarp m) {
        m.setEstado(new Enfriamiento());
    }

    @Override public void finalizarSalto(MotorWarp m) {
        m.setEstado(new Disponible());
    }

    @Override public String getNombre() {
        return "En warp";
    }

}
