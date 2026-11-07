package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.vista.PanelControl;
import unmdp.fi.programacionc.vista.PanelCreacion;
import unmdp.fi.programacionc.vista.PanelEnConstruccion;
import unmdp.fi.programacionc.vista.PanelTripulantes;
import unmdp.fi.programacionc.vista.VentanaPrincipal;

/**
 * Arma la ventana principal: crea cada seccion con su controlador y las agrega como pestanias.
 * Todas las secciones comparten la misma Sesion.
 */
public class ControladorPrincipal {

    private final VentanaPrincipal ventana;

    /**
     * pre -> sesion != null
     */
    public ControladorPrincipal(Sesion sesion) {
        assert sesion != null : "El controlador principal necesita la sesion";
        this.ventana = new VentanaPrincipal(sesion.getUsuario());
        // Por ahora vacias: se van reemplazando por la seccion real (panel + controlador) una por una
        PanelCreacion creacion = new PanelCreacion();
        new ControladorCreacion(creacion, sesion);
        ventana.agregarSeccion("Creacion", creacion);
        PanelTripulantes tripulantes = new PanelTripulantes();
        new ControladorTripulantes(tripulantes, sesion);
        ventana.agregarSeccion("Tripulantes", tripulantes);
        PanelControl control = new PanelControl();
        new ControladorControl(control, sesion);
        ventana.agregarSeccion("Control de la nave", control);
        ventana.agregarSeccion("Visor", new PanelEnConstruccion("Visor"));
    }

    public void mostrar() {
        ventana.mostrar();
    }
}