package unmdp.fi.programacionc.motor;

public class PreparandoSalto implements EstadoMotor {

    @Override
    public void confirmarSalto(MotorWarp motor) {
        motor.setEstado(new Warp());
    }

    @Override
    public void abortarSalto(MotorWarp motor) {
        motor.setEstado(new Disponible());
    }

    @Override
    public String getNombre() {
        return "Preparando salto";
    }
}
