package unmdp.fi.programacionc.motor;

/**
 * Estado "Preparando salto".
 * saltar: salto simple; si hay energia para el warp -> Warp, si no se queda.
 */
public class PreparandoSalto implements EstadoMotor {

    @Override
    public int saltar(MotorWarp motor) {
        int avanzados = motor.avanzarUnCasillero();
        if (motor.hayEnergiaParaWarp()) {
            motor.setEstado(new Warp());
        }
        return avanzados;
    }

    @Override
    public String getNombre() {
        return "Preparando salto";
    }
}
