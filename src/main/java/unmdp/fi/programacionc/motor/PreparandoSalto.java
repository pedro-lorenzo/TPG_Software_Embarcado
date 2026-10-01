package unmdp.fi.programacionc.motor;

public class PreparandoSalto implements EstadoMotor {

    @Override public void saltar(MotorWarp m) {
        m.setEstado(new Warp());
    }

    @Override public String getNombre() {
        return "Preparando salto";
    }
}
