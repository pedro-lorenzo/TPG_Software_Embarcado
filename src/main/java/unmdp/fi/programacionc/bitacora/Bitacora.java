package unmdp.fi.programacionc.bitacora;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Bitacora de un asistente de comando (Aclaracion R5): eventos en orden temporal, con cuando
 * sucedieron y que paso. Tambien es el informe de las misiones: cada una deja alli su resultado.
 *
 * Solo permite agregar eventos: no hay forma de borrarlos ni editarlos, y cada evento es inmutable.
 * No imprime nada: la presentacion queda a cargo de quien la consulta (Main o la futura interfaz).
 *
 * Es segura entre hilos: la mision registra eventos desde su propio hilo mientras la interfaz
 * los lee. La lista es concurrente (recorrerla nunca falla aunque se agreguen eventos) y
 * registrar() es synchronized para que el numero de secuencia no se repita ni se salteee.
 *
 * inv -> los eventos estan ordenados por numero de secuencia creciente, empezando en 1
 */
public class Bitacora {

    private final List<EventoBitacora> eventos = new CopyOnWriteArrayList<>();
    private long proximoNumero = 1;

    /**
     * Los eventos los arma codigo propio (asistente y misiones), por eso sus datos se validan
     * como precondicion (assert) y no con excepciones.
     *
     * pre -> tipo != null
     * pre -> descripcion != null y no vacia
     * post -> el evento queda al final, con el siguiente numero de secuencia y la fecha actual
     */
    public synchronized void registrar(TipoEvento tipo, String descripcion) {
        assert tipo != null : "El tipo de evento no puede ser nulo";
        assert descripcion != null && !descripcion.trim().isEmpty() : "La descripcion del evento no puede ser nula ni vacia";
        this.eventos.add(new EventoBitacora(this.proximoNumero++, LocalDateTime.now(), tipo, descripcion));
    }

    /** Eventos en orden temporal. La lista no se puede modificar. */
    public List<EventoBitacora> getEventos() {
        return Collections.unmodifiableList(this.eventos);
    }

    /**
     * Eventos de un tipo, en orden temporal. La lista no se puede modificar.
     * pre -> tipo != null
     */
    public List<EventoBitacora> getEventosPorTipo(TipoEvento tipo) {
        assert tipo != null : "El tipo de evento no puede ser nulo";
        List<EventoBitacora> filtrados = new ArrayList<>();
        for (EventoBitacora evento : this.eventos) {
            if (evento.getTipo() == tipo) {
                filtrados.add(evento);
            }
        }
        return Collections.unmodifiableList(filtrados);
    }

    public int cantidadEventos() {
        return this.eventos.size();
    }
}