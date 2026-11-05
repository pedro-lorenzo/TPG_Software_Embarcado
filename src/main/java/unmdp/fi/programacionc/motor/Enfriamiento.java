package unmdp.fi.programacionc.motor;

/**
 * Estado "Enfriamiento" (tras un salto warp).
 * saltar: salto simple mas DESGASTE_ENFRIAMIENTO de desgaste -> Disponible.
 */
public class Enfriamiento implements EstadoMotor {

    @Override
    public int saltar(MotorWarp motor) {
        int avanzados = motor.avanzarUnCasilleroCobrandoEnfriamiento();
        motor.setEstado(new Disponible());
        return avanzados;
    }

    @Override
    public String getNombre() {
        return "Enfriamiento";
    }
}
