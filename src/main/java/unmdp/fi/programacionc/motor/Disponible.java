package unmdp.fi.programacionc.motor;

public class Disponible implements EstadoMotor {

    @Override
    public void iniciarPreparacion(MotorWarp motor) {
        motor.setEstado(new PreparandoSalto());
    }

    @Override
    public String getNombre() {
        return "Disponible";
    }
}
