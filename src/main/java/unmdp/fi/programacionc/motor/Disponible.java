package unmdp.fi.programacionc.motor;

public class Disponible implements EstadoMotor {

    @Override public void prepararSalto(MotorWarp m) {
        m.setEstado(new PreparandoSalto());
    }

    @Override public boolean estaDisponible() {
        return true;
    }

    @Override public String getNombre() {
        return "Disponible";
    }
}