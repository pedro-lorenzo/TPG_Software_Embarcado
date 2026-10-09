package unmdp.fi.programacionc.bitacora;

import java.time.LocalDateTime;

/**
 * Evento registrado en una bitacora. Inmutable (Aclaracion R5): campos final y sin metodos
 * que lo modifiquen. Solo lo crea Bitacora (constructor package-private).
 *
 * inv -> numero > 0, fecha != null, tipo != null, descripcion no vacia
 */
public final class EventoBitacora {

    private final long numero;
    private final LocalDateTime fecha;
    private final TipoEvento tipo;
    private final String descripcion;

    /**
     * pre -> datos validos segun el invariante (los garantiza Bitacora)
     */
    EventoBitacora(long numero, LocalDateTime fecha, TipoEvento tipo, String descripcion) {
        this.numero = numero;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public long getNumero()          { return numero; }
    public LocalDateTime getFecha()  { return fecha; }
    public TipoEvento getTipo()      { return tipo; }
    public String getDescripcion()   { return descripcion; }

    @Override
    public String toString() {
        return "#" + numero + " " + fecha + " [" + tipo + "] " + descripcion;
    }
}
