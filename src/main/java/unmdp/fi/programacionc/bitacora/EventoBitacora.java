package unmdp.fi.programacionc.bitacora;
import java.time.LocalDateTime;
public final class EventoBitacora {
    private final long numero;
    private final LocalDateTime fecha;
    private final String tipo;
    private final String descripcion;

    EventoBitacora(long numero, LocalDateTime fecha, String tipo, String descripcion) {
        this.numero = numero; this.fecha = fecha; this.tipo = tipo; this.descripcion = descripcion;
    }
    public long getNumero()          { return numero; }
    public LocalDateTime getFecha()  { return fecha; }
    public String getTipo()      { return tipo; }
    public String getDescripcion()   { return descripcion; }
    @Override public String toString() { return "#" + numero + " " + fecha + " [" + tipo + "] " + descripcion; }
}