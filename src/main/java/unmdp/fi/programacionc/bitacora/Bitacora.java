package unmdp.fi.programacionc.bitacora;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Subsistema de la Nave (composicion). Solo permite agregar eventos: no hay forma de
// borrarlos ni editarlos, asi el orden temporal no se puede romper.
// No imprime nada: la presentacion queda a cargo del main o de la futura GUI.
// Los eventos siempre los arma codigo propio (asistente, misiones), por eso sus
// datos se validan como precondicion con assert y no con excepciones.
public class Bitacora {

    private final List<EventoBitacora> eventos = new ArrayList<>();
    private long proximoNumero = 1;

    /**
     * pre -> tipo != null
     * pre -> descripcion != null y no vacia
     * post -> el evento queda al final de la lista con el siguiente numero de secuencia
     */
    public void registrar(String tipo, String descripcion) {
        assert TipoEvento.esValido(tipo) : "Tipo de evento invalido: " + tipo;
        assert descripcion != null && !descripcion.trim().isEmpty() : "La descripcion del evento no puede ser nula ni vacia";
        this.eventos.add(new EventoBitacora(this.proximoNumero++, LocalDateTime.now(), tipo, descripcion));
    }

    // Devuelve los eventos en orden temporal (orden de registro), sin permitir modificarlos
    public List<EventoBitacora> getEventos() {
        return Collections.unmodifiableList(this.eventos);
    }

    /**
     * pre -> tipo != null
     */
    public List<EventoBitacora> getEventosPorTipo(String tipo) {
        assert TipoEvento.esValido(tipo) : "Tipo de evento invalido: " + tipo;
        List<EventoBitacora> filtrados = new ArrayList<>();
        for (EventoBitacora evento : this.eventos) {
            if (evento.getTipo().equals(tipo)) {      // antes: ==
                filtrados.add(evento);
            }
        }
        return Collections.unmodifiableList(filtrados);
    }

    public int cantidadEventos() {
        return this.eventos.size();
    }
}
