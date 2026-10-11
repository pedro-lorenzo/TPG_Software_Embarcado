package unmdp.fi.programacionc.motor;

/**
 * Motor warp: contexto del patron State (Aclaracion R3).
 *
 * No decide transiciones: delega cada accion en el estado actual, que es el unico que puede
 * reemplazarlo (setEstado es package-private). Agregar un estado es agregar una clase que
 * implemente EstadoMotor; ni el motor ni la nave se llenan de condicionales.
 *
 * Transiciones validas:
 *   Disponible       --prepararSalto-->         Preparando salto
 *   Preparando salto --saltar-->                En warp
 *   En warp          --enfriar-->               Enfriamiento
 *   En warp          --finalizarSalto-->        Disponible   (sin paso del tiempo, Aclaracion R3)
 *   Enfriamiento     --completarEnfriamiento--> Disponible
 * Cualquier otra accion lanza OperacionInvalidaException y el estado no cambia.
 *
 * inv -> estado != null
 * El motor arranca en Disponible.
 */
public class MotorWarp {

    private EstadoMotor estado = new Disponible();

    public void prepararSalto()         { estado.prepararSalto(this); }
    public void saltar()                { estado.saltar(this); }
    public void enfriar()               { estado.enfriar(this); }
    public void finalizarSalto()        { estado.finalizarSalto(this); }
    public void completarEnfriamiento() { estado.completarEnfriamiento(this); }

    public boolean estaDisponible() { return estado.estaDisponible(); }
    public String getEstadoActual() { return estado.getNombre(); }

    /**
     * Solo lo usan los estados (mismo paquete) para concretar una transicion.
     * pre -> nuevo != null
     */
    void setEstado(EstadoMotor nuevo) {
        assert nuevo != null : "El motor siempre tiene un estado";
        this.estado = nuevo;
    }
}
