package unmdp.fi.programacionc.motor;

/**
 * Estado "Warp": motor listo, esperando el salto warp.
 * saltar: salto simple, se queda en Warp.
 * saltarWarp: con energia suficiente salto warp -> Enfriamiento; sin energia aborta,
 * hace el salto simple de ese turno y vuelve a Preparando salto.
 */
public class Warp implements EstadoMotor {

    @Override
    public int saltar(MotorWarp motor) {
        return motor.avanzarUnCasillero();
    }

    @Override
    public int saltarWarp(MotorWarp motor) {
        if (motor.hayEnergiaParaWarp()) {
            int avanzados = motor.ejecutarSaltoWarp();
            motor.setEstado(new Enfriamiento());
            return avanzados;
        }
        int avanzados = motor.avanzarUnCasillero();
        motor.setEstado(new PreparandoSalto());
        return avanzados;
    }

    @Override
    public String getNombre() {
        return "En warp";
    }
}
