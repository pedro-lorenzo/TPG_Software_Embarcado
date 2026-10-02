package unmdp.fi.programacionc.haberes;

/**
 * Un renglon de la liquidacion: que se paga y cuanto.
 * inv -> nombre != null y no vacio
 * inv -> importe es un numero finito y >= 0
 */
public final class ConceptoHaber {
    private final String nombre;
    private final double importe;

    public ConceptoHaber(String nombre, double importe) {
        assert nombre != null && !nombre.trim().isEmpty() : "El concepto necesita un nombre";
        assert !Double.isNaN(importe) && !Double.isInfinite(importe) && importe >= 0 : "Importe invalido: " + importe;
        this.nombre = nombre;
        this.importe = importe;
    }

    public String getNombre()  { return nombre; }
    public double getImporte() { return importe; }
}