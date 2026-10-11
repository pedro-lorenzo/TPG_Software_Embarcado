package unmdp.fi.programacionc.motor;

/**
 * Estado inicial del motor. Unico estado en el que la nave puede salir de mision.
 * Accion valida: prepararSalto -> Preparando salto.
 */
public class Disponible implements EstadoMotor {

    @Override
    public void prepararSalto(MotorWarp motor) {
        motor.setEstado(new PreparandoSalto());
    }

    @Override
    public boolean estaDisponible() {
        return true;
    }

    @Override
    public String getNombre() {
        return "Disponible";
    }
}
