package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.ObservadorSesion;
import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.vista.IVistaControl;
import unmdp.fi.programacionc.mision.InformeMision;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Seccion de Control de la nave. Toda orden pasa por el asistente de la nave actual (R2).
 *  - ActionListener: cargas y mantenimiento.
 *  - ObservadorSesion: cuando cambia la nave, refresca recursos y bitacora.
 */
public class ControladorControl implements ActionListener, ObservadorSesion {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final IVistaControl vista;
    private final Sesion sesion;

    /**
     * pre -> vista != null y sesion != null
     */
    public ControladorControl(IVistaControl vista, Sesion sesion) {
        assert vista != null && sesion != null : "El controlador necesita vista y sesion";
        this.vista = vista;
        this.sesion = sesion;
        vista.setActionListener(this);
        sesion.agregarObservador(this);
        sesionActualizada();
    }

    // ===================== acciones del usuario =====================

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (!sesion.tieneNave()) {
            return;
        }
        Asistente asistente = sesion.getNaveActual().getAsistente();
        try {
            switch (evento.getActionCommand()) {
                case IVistaControl.CARGAR_COMBUSTIBLE:
                    asistente.cargarCombustible(leerCantidad());
                    vista.limpiarCantidad();
                    break;
                case IVistaControl.CARGAR_ENERGIA:
                    asistente.cargarEnergia(leerCantidad());
                    vista.limpiarCantidad();
                    break;
                case IVistaControl.MANTENIMIENTO:
                    asistente.realizarMantenimiento();
                    break;
                default:
                    return;
            }
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
        }
        // Haya salido bien o no, la bitacora cambio (el asistente registra tambien los rechazos)
        sesion.notificar();
    }

    /**
     * La carga positiva es precondicion de Tanque (assert): como el dato viene del usuario,
     * el controlador la garantiza antes de llamar, avisando con un error si no se cumple.
     */
    private int leerCantidad() {
        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.getCantidad().trim());
        } catch (NumberFormatException e) {
            throw new OperacionInvalidaException("La cantidad tiene que ser un numero entero");
        }
        if (cantidad <= 0) {
            throw new OperacionInvalidaException("La cantidad tiene que ser mayor a cero");
        }
        return cantidad;
    }

    // ===================== Observer: la sesion cambio =====================

    @Override
    public void sesionActualizada() {
        if (!sesion.tieneNave()) {
            vista.sinNave();
            vista.mostrarInformes(new ArrayList<>());
            vista.habilitarAcciones(false);
            return;
        }
        Nave nave = sesion.getNaveActual();
        Asistente asistente = nave.getAsistente();

        // ... (todo igual hasta vista.mostrarBitacora(lineas);)

        List<String> informes = new ArrayList<>();
        for (InformeMision informe : asistente.getInformes()) {
            informes.add(informe.getCodigo() + " - " + informe.resumen());
        }
        vista.mostrarInformes(informes);

        vista.habilitarAcciones(!asistente.estaDestruida());
    }
}
