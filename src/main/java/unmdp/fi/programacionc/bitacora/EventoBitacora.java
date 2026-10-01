package unmdp.fi.programacionc.bitacora;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Evento inmutable: una vez registrado no se puede modificar.
// El numero de secuencia garantiza el orden aunque dos eventos tengan la misma fechaHora.
public class EventoBitacora {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final long numero;
    private final LocalDateTime fechaHora;
    private final TipoEvento tipo;
    private final String descripcion;

    // package-private: solo la Bitacora crea eventos, asi controla la validacion y la numeracion
    EventoBitacora(long numero, LocalDateTime fechaHora, TipoEvento tipo, String descripcion) {
        this.numero = numero;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public long getNumero() {
        return this.numero;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }

    public TipoEvento getTipo() {
        return this.tipo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    @Override
    public String toString() {
        return "#" + numero + " [" + fechaHora.format(FORMATO_HORA) + "] " + tipo + " - " + descripcion;
    }
}
