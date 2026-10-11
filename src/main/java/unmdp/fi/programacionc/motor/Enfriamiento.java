package unmdp.fi.programacionc.motor;

/**
 * Estado "Enfriamiento". Existe aunque las misiones todavia no lo usen (Aclaracion R3).
 * Accion valida: completarEnfriamiento -> Disponible.
 */
public class Enfriamiento implements EstadoMotor {

    @Override
    public void completarEnfriamiento(MotorWarp motor) {
        motor.setEstado(new Disponible());
    }

    @Override
    public String getNombre() {
        return "Enfriamiento";
    }
}
