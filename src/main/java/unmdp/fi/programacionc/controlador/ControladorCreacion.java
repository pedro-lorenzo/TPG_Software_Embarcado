package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.ObservadorSesion;
import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.nave.TipoNave;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.vista.IVistaCreacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Seccion de Creacion.
 *  - Como ActionListener (MVC): atiende los botones, traduce los textos de la vista
 *    a tipos del modelo y llama a la Sesion.
 *  - Como ObservadorSesion (Observer): cuando la sesion cambia, refresca la vista.
 * A la vista solo le pasa textos, nunca objetos del modelo.
 */
public class ControladorCreacion implements ActionListener, ObservadorSesion {

    private final IVistaCreacion vista;
    private final Sesion sesion;

    /**
     * pre -> vista != null y sesion != null
     */
    public ControladorCreacion(IVistaCreacion vista, Sesion sesion) {
        assert vista != null && sesion != null : "El controlador necesita vista y sesion";
        this.vista = vista;
        this.sesion = sesion;
        vista.cargarOpciones(nombres(TipoNave.values()), nombres(Cargo.values()), nombres(Origen.values()));
        vista.setActionListener(this);
        sesion.agregarObservador(this);
        sesionActualizada();   // estado inicial de la pantalla
    }

    // ===================== acciones del usuario =====================

    @Override
    public void actionPerformed(ActionEvent evento) {
        try {
            if (IVistaCreacion.CREAR_NAVE.equals(evento.getActionCommand())) {
                sesion.crearNave(TipoNave.valueOf(vista.getTipoNave()));
            } else if (IVistaCreacion.AGREGAR_TRIPULANTE.equals(evento.getActionCommand())) {
                agregarTripulante();
            }
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
        }
        // si salio bien no hace falta tocar la vista aca: la sesion avisa y se refresca sola
    }

    private void agregarTripulante() {
        int antiguedad;
        try {
            antiguedad = Integer.parseInt(vista.getAntiguedad().trim());
        } catch (NumberFormatException e) {
            vista.mostrarError("La antiguedad tiene que ser un numero entero");
            return;
        }
        sesion.agregarTripulante(vista.getNombre(), Cargo.valueOf(vista.getCargo()),
                Origen.valueOf(vista.getOrigen()), antiguedad);
        vista.limpiarTripulante();
    }

    // ===================== Observer: la sesion cambio =====================

    @Override
    public void sesionActualizada() {
        vista.habilitarTripulantes(sesion.tieneNave());
        if (!sesion.tieneNave()) {
            vista.mostrarNave("Todavia no hay una nave en uso");
            vista.mostrarTripulantes(new ArrayList<>());
            vista.mostrarEstadoTripulacion("Crea una nave para cargar la tripulacion", false);
            return;
        }
        Nave nave = sesion.getNaveActual();
        vista.mostrarNave("Nave #" + nave.getId() + " - " + nave.getTipo());
        List<String> filas = new ArrayList<>();
        for (Tripulante t : nave.getTripulantes()) {
            filas.add(t.getNombre() + "  |  " + t.getCargo() + "  |  " + t.getOrigen()
                    + "  |  " + t.getAntiguedad() + " anios");
        }
        vista.mostrarTripulantes(filas);
        int cantidad = nave.getTripulantes().size();
        if (nave.tieneTripulacionValida()) {
            vista.mostrarEstadoTripulacion("Tripulacion valida (" + cantidad + ")", true);
        } else {
            vista.mostrarEstadoTripulacion("Tripulacion incompleta (" + cantidad + ")", false);
        }
    }

    private static String[] nombres(Enum<?>[] valores) {
        String[] nombres = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            nombres[i] = valores[i].name();
        }
        return nombres;
    }
}