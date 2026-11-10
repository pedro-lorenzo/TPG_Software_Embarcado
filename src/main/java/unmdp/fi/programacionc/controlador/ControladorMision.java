package unmdp.fi.programacionc.controlador;

import unmdp.fi.programacionc.aplicacion.ObservadorSesion;
import unmdp.fi.programacionc.aplicacion.Sesion;
import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.tablero.ElementoDelUniverso;
import unmdp.fi.programacionc.vista.IVistaMision;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Seccion de Mision.
 *  - ActionListener: "Iniciar mision". La Sesion la corre en otro hilo, asi la ventana no se congela.
 *  - ObservadorSesion: en cada turno del tablero y al terminar, refresca el tablero y el estado.
 *    La Sesion avisa en el hilo de Swing porque ControladorPrincipal le configura invokeLater.
 */
public class ControladorMision implements ActionListener, ObservadorSesion {

    // Mismo orden en los dos arreglos: lo que ve el usuario y el codigo que entiende la Sesion
    private static final String[] NOMBRES = {
            "M-01  Intercepcion y asistencia", "M-02  Recoleccion", "M-03  Retorno seguro"};
    private static final String[] CODIGOS = {
            Sesion.MISION_INTERCEPCION, Sesion.MISION_RECOLECCION, Sesion.MISION_RETORNO};

    private final IVistaMision vista;
    private final Sesion sesion;

    /**
     * pre -> vista != null y sesion != null
     */
    public ControladorMision(IVistaMision vista, Sesion sesion) {
        assert vista != null && sesion != null : "El controlador necesita vista y sesion";
        this.vista = vista;
        this.sesion = sesion;
        vista.cargarMisiones(NOMBRES);
        vista.setActionListener(this);
        sesion.agregarObservador(this);
        sesionActualizada();
    }

    // ===================== acciones del usuario =====================

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (!IVistaMision.INICIAR.equals(evento.getActionCommand())) {
            return;
        }
        int elegida = vista.getMisionElegida();
        if (elegida < 0) {
            return;
        }
        try {
            sesion.iniciarMision(CODIGOS[elegida]);   // vuelve enseguida: la mision sigue en otro hilo
        } catch (OperacionInvalidaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    // ===================== Observer: la sesion cambio =====================

    @Override
    public void sesionActualizada() {
        if (!sesion.tieneNave()) {
            vista.limpiarTablero();
            vista.mostrarEstado("Crea una nave para poder salir de mision", true);
            vista.habilitarInicio(false);
            return;
        }
        mostrarTablero(sesion.getTablero());

        Nave nave = sesion.getNaveActual();
        Asistente asistente = nave.getAsistente();
        if (sesion.hayMisionEnCurso()) {
            vista.mostrarEstado("Mision en curso...", false);
            vista.habilitarInicio(false);
        } else if (asistente.estaDestruida()) {
            vista.mostrarEstado("La nave fue destruida", true);
            vista.habilitarInicio(false);
        } else if (!nave.tieneTripulacionValida()) {
            // "Verificar mando antes de operar": sin tripulacion valida no se sale de mision
            vista.mostrarEstado("Tripulacion incompleta", true);
            vista.habilitarInicio(false);
        } else {
            vista.mostrarEstado("Lista para salir", false);
            vista.habilitarInicio(true);
        }
    }

    /**
     * Saca una "foto" del tablero y se la pasa a la vista como simbolos.
     * Se lee dentro de synchronized (tablero) porque la mision lo modifica desde su hilo
     * (Travesia toma el mismo candado en cada turno): asi la foto nunca queda a medio turno.
     */
    private void mostrarTablero(Tablero tablero) {
        char[][] celdas;
        Posicion nave;
        synchronized (tablero) {
            ElementoDelUniverso[][] elementos = tablero.verTablero();
            nave = tablero.getPosicionNave();
            celdas = new char[elementos.length][];
            for (int x = 0; x < elementos.length; x++) {
                celdas[x] = new char[elementos[x].length];
                for (int y = 0; y < elementos[x].length; y++) {
                    celdas[x][y] = elementos[x][y] == null ? ' ' : elementos[x][y].getSimbolo();
                }
            }
        }
        vista.mostrarTablero(celdas, nave.getX(), nave.getY());
    }
}