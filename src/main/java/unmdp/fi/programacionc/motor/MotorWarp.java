package unmdp.fi.programacionc.motor;

// Contexto del patron State. No decide transiciones: delega siempre en el
// estado actual y este es el unico que puede reemplazarlo (setEstado package-private).
public class MotorWarp {

    private EstadoMotor estado = new Disponible();

    /*prepararSalto(): Disponible → PreparandoSalto
    saltar(): PreparandoSalto → Warp
    enfriar(): Warp → Enfriamiento
    finalizarSalto(): Warp → Disponible*/

    public void prepararSalto() {
        estado.iniciarPreparacion(this);
    }

    public void saltar() {
        estado.confirmarSalto(this);
    }

    public void enfriar() {
        estado.completarEnfriamiento(this);
    }

    void setEstado(EstadoMotor nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getEstadoActual() {
        return estado.getNombre();
    }

}
