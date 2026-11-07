package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.concurrencia.modelo.UniversoParalelo;
import unmdp.fi.programacionc.vista.PanelControl;
import unmdp.fi.programacionc.vista.PanelCreacion;
import unmdp.fi.programacionc.vista.PanelTripulantes;
import unmdp.fi.programacionc.vista.PanelVisor;
import unmdp.fi.programacionc.vista.VentanaPrincipal;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        // Cada seccion: su panel (vista) y su controlador, que se engancha a la vista y al modelo
        PanelCreacion creacion = new PanelCreacion();
        new ControladorCreacion(creacion, sesion);
        ventana.agregarSeccion("Creacion", creacion);
        PanelTripulantes tripulantes = new PanelTripulantes();
        new ControladorTripulantes(tripulantes, sesion);
        ventana.agregarSeccion("Tripulantes", tripulantes);
        PanelControl control = new PanelControl();
        new ControladorControl(control, sesion);
        ventana.agregarSeccion("Control de la nave", control);
        PanelVisor visor = new PanelVisor();
        UniversoParalelo universoParalelo = new UniversoParalelo();
        new ControladorVisor(visor, universoParalelo);
        ventana.agregarSeccion("Visor", visor);

        // Al cerrar la ventana se detienen los hilos del universo paralelo
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                universoParalelo.detener();
            }
        });
    }

    public void mostrar() {
        ventana.mostrar();
    }
}