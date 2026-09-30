package unmdp.fi.programacionc.motor;

public class Warp implements EstadoMotor {
    /*
    lo borramos en MotorWarp
    @Override
    public void finalizarSalto(MotorWarp motor) {
        motor.setEstado(new Enfriamiento());
    }

    @Override
    public void abortarSalto(MotorWarp motor) {
        // abortar en pleno salto igual requiere enfriamiento del motor
        motor.setEstado(new Enfriamiento());
    }
    */
    @Override
    public String getNombre() {
        return "En warp";
    }
}
