package unmdp.fi.programacionc.motor;

/**
 * Estado inicial del motor. Unico estado en el que la nave puede salir de mision.
 * saltar: salto simple -> Preparando salto.
 */
public class Disponible implements EstadoMotor {

    @Override
    public int saltar(MotorWarp motor) {
        int avanzados = motor.avanzarUnCasillero();
        motor.setEstado(new PreparandoSalto());
        return avanzados;
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
