package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.vista.PanelEnConstruccion;
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
        ventana.agregarSeccion("Creacion", new PanelEnConstruccion("Creacion"));
        ventana.agregarSeccion("Tripulantes", new PanelEnConstruccion("Tripulantes"));
        ventana.agregarSeccion("Control de la nave", new PanelEnConstruccion("Control de la nave"));
        ventana.agregarSeccion("Visor", new PanelEnConstruccion("Visor"));
    }

    public void mostrar() {
        ventana.mostrar();
    }
}
