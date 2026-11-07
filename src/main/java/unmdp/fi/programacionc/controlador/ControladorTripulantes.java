package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.ObservadorSesion;
import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.haberes.ComponenteHaber;
import unmdp.fi.programacionc.haberes.LiquidadorHaberes;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.vista.IVistaTripulantes;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Seccion de Tripulantes. Liquida el salario de cada tripulante con LiquidadorHaberes
 * (la cadena de decoradores de la Parte 1) y le pasa a la vista los conceptos ya convertidos en texto.
 *  - ActionListener: boton "Registrar consejo".
 *  - ListSelectionListener: el usuario eligio otra fila.
 *  - ObservadorSesion: cambio la tripulacion o los consejos; se refresca sola.
 */
public class ControladorTripulantes implements ActionListener, ListSelectionListener, ObservadorSesion {

    private final IVistaTripulantes vista;
    private final Sesion sesion;
    private List<Tripulante> mostrados = new ArrayList<>();   // fila i de la tabla = mostrados.get(i)

    /**
     * pre -> vista != null y sesion != null
     */
    public ControladorTripulantes(IVistaTripulantes vista, Sesion sesion) {
        assert vista != null && sesion != null : "El controlador necesita vista y sesion";
        this.vista = vista;
        this.sesion = sesion;
        vista.setActionListener(this);
        vista.setSeleccionListener(this);
        sesion.agregarObservador(this);
        sesionActualizada();
    }

    // ===================== acciones del usuario =====================

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (!IVistaTripulantes.REGISTRAR_CONSEJO.equals(evento.getActionCommand())) {
            return;
        }
        Tripulante elegido = elegido();
        if (elegido == null) {
            return;
        }
        try {
            sesion.registrarConsejo(elegido);   // la sesion avisa y la tabla se refresca sola
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting()) {
            mostrarDetalleDelElegido();
        }
    }

    // ===================== Observer: la sesion cambio =====================

    @Override
    public void sesionActualizada() {
        int filaAntes = vista.getFilaSeleccionada();
        mostrados = sesion.tieneNave()
                ? new ArrayList<>(sesion.getNaveActual().getTripulantes())
                : Collections.emptyList();
        List<String[]> filas = new ArrayList<>();
        for (Tripulante t : mostrados) {
            filas.add(new String[]{
                    t.getNombre(), t.getCargo().name(), t.getOrigen().name(),
                    t.getAntiguedad() + " anios",
                    t.estaFallecido() ? "Fallecido" : "Activo",
                    pg(liquidar(t).getImporte())});
        }
        vista.mostrarTripulantes(filas);
        vista.seleccionarFila(filaAntes);   // conserva la fila elegida despues de refrescar
        mostrarDetalleDelElegido();
    }

    // ===================== auxiliares =====================

    private void mostrarDetalleDelElegido() {
        Tripulante t = elegido();
        if (t == null) {
            vista.limpiarDetalle(mostrados.isEmpty() ? "Todavia no hay tripulantes" : "Elegi un tripulante");
            vista.habilitarConsejo(false);
            return;
        }
        ComponenteHaber haber = liquidar(t);
        // La cadena va del ultimo decorador al haber base: se invierte para mostrarla en orden
        List<String[]> conceptos = new ArrayList<>();
        for (ComponenteHaber c = haber; c != null; c = c.getInterno()) {
            conceptos.add(0, new String[]{c.getConcepto(), pg(c.getAporte())});
        }
        vista.mostrarDetalle(t.getNombre(), conceptos, pg(haber.getImporte()));
        vista.habilitarConsejo(t.getCargo() == Cargo.CONSEJERO && !t.estaFallecido());
    }

    private Tripulante elegido() {
        int fila = vista.getFilaSeleccionada();
        return fila >= 0 && fila < mostrados.size() ? mostrados.get(fila) : null;
    }

    private ComponenteHaber liquidar(Tripulante t) {
        return LiquidadorHaberes.liquidar(t, sesion.getConsejos(t));
    }

    private static String pg(double importe) {
        return String.format("%.2f PG", importe);
    }
}
