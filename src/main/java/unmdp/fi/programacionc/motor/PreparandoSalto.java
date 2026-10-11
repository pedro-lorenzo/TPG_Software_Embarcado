package unmdp.fi.programacionc.motor;

/**
 * Estado "Preparando salto".
 * Accion valida: saltar -> En warp.
 */
public class PreparandoSalto implements EstadoMotor {

    @Override
    public void saltar(MotorWarp motor) {
        motor.setEstado(new Warp());
    }

    @Override
    public String getNombre() {
        return "Preparando salto";
    }
}
