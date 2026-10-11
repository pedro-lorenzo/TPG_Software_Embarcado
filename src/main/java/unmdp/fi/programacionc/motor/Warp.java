package unmdp.fi.programacionc.motor;

/**
 * Estado "En warp" (la nave esta saltando).
 * Acciones validas: enfriar -> Enfriamiento; finalizarSalto -> Disponible
 * (mientras no se modele el paso del tiempo, Aclaracion R3).
 */
public class Warp implements EstadoMotor {

    @Override
    public void enfriar(MotorWarp motor) {
        motor.setEstado(new Enfriamiento());
    }

    @Override
    public void finalizarSalto(MotorWarp motor) {
        motor.setEstado(new Disponible());
    }

    @Override
    public String getNombre() {
        return "En warp";
    }
}
