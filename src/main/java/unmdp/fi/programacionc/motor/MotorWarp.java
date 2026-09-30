package unmdp.fi.programacionc.motor;

/*  prepararSalto(): Disponible → PreparandoSalto
    saltar(): PreparandoSalto → Warp
    enfriar(): Warp → Enfriamiento
    finalizarSalto(): Warp → Disponible*/

// Contexto del patron State. No decide transiciones: delega siempre en el
// estado actual y este es el unico que puede reemplazarlo (setEstado package-private).
public class MotorWarp {
    private EstadoMotor estado = new Disponible();

    public void prepararSalto()        {estado.prepararSalto(this); }
    public void saltar()               { estado.saltar(this); }
    public void enfriar()              { estado.enfriar(this); }
    public void finalizarSalto()       { estado.finalizarSalto(this); }
    public void completarEnfriamiento(){ estado.completarEnfriamiento(this); }

    void setEstado(EstadoMotor nuevo)  { this.estado = nuevo; }
    public boolean estaDisponible()    { return estado.estaDisponible(); }
    public String getEstadoActual()    { return estado.getNombre(); }
}
