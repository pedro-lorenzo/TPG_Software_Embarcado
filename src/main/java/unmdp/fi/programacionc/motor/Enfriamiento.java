package unmdp.fi.programacionc.motor;

public class Enfriamiento implements EstadoMotor {

    @Override public void completarEnfriamiento(MotorWarp m) {
        m.setEstado(new Disponible());
    }
    @Override public String getNombre() {
        return "Enfriamiento";
    }

}