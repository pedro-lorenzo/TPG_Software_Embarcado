package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.concurrencia.modelo.UniversoParalelo;
import unmdp.fi.programacionc.concurrencia.observer.ObservadorVisor;
import unmdp.fi.programacionc.vista.IVistaVisor;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Visor del universo paralelo.
 *  - ActionListener (MVC): los dos botones crean generadoras y naves en vivo.
 *  - ObservadorVisor (Observer): recibe cada evento de la simulacion y lo muestra.
 *
 * Los eventos llegan desde los hilos de las generadoras y las naves, NO desde el hilo de Swing.
 * Swing no se puede tocar desde otro hilo, asi que cada evento se encola con invokeLater.
 */
public class ControladorVisor implements ActionListener, ObservadorVisor {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final IVistaVisor vista;
    private final UniversoParalelo universo;
    private int generadoras = 0;
    private int naves = 0;

    /**
     * pre -> vista != null y universo != null
     */
    public ControladorVisor(IVistaVisor vista, UniversoParalelo universo) {
        assert vista != null && universo != null : "El controlador necesita vista y universo";
        this.vista = vista;
        this.universo = universo;
        vista.setActionListener(this);
        universo.agregarObservador(this);
        vista.mostrarContadores(0, 0);
    }

    // ===================== acciones del usuario (hilo de Swing) =====================

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (IVistaVisor.CREAR_GENERADORA.equals(evento.getActionCommand())) {
            universo.agregarGeneradora();
            generadoras++;
        } else if (IVistaVisor.CREAR_NAVE.equals(evento.getActionCommand())) {
            universo.agregarNave();
            naves++;
        }
        vista.mostrarContadores(generadoras, naves);
    }

    // ===================== Observer (hilos de la simulacion) =====================

    @Override
    public void actualizar(String evento) {
        // La hora se toma ahora, cuando ocurrio; el dibujo lo hace despues el hilo de Swing
        String linea = "[" + LocalTime.now().format(HORA) + "] " + evento;
        SwingUtilities.invokeLater(() -> vista.agregarEvento(linea));
    }
}
