package unmdp.fi.programacionc.motor;

// Contexto del patron State. No decide transiciones: delega siempre en el
// estado actual y este es el unico que puede reemplazarlo (setEstado package-private).
public class MotorWarp {

    private EstadoMotor estado = new Disponible();

    public void iniciarPreparacion() {
        estado.iniciarPreparacion(this);
    }

    public void confirmarSalto() {
        estado.confirmarSalto(this);
    }

    public void finalizarSalto() {
        estado.finalizarSalto(this);
    }

    public void abortarSalto() {
        estado.abortarSalto(this);
    }

    public void completarEnfriamiento() {
        estado.completarEnfriamiento(this);
    }

    void setEstado(EstadoMotor nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getEstadoActual() {
        return estado.getNombre();
    }
}
