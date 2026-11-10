package unmdp.fi.programacionc.vista;

import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Lo que el controlador puede pedirle a la seccion de Tripulantes.
 * Como las demas vistas, solo recibe textos.
 */
public interface IVistaTripulantes {

    String REGISTRAR_CONSEJO = "REGISTRAR_CONSEJO";

    /** Una fila por tripulante: nombre, cargo, origen, antiguedad, estado y salario. */
    void mostrarTripulantes(List<String[]> filas);

    /** Indice de la fila elegida, o -1 si no hay ninguna. */
    int getFilaSeleccionada();
    void seleccionarFila(int fila);

    /** Detalle del salario: un par {concepto, importe} por linea, y el total. */
    void mostrarDetalle(String titulo, List<String[]> conceptos, String total);
    void limpiarDetalle(String mensaje);
    void habilitarConsejo(boolean habilitado);
    void mostrarError(String mensaje);

    void setActionListener(ActionListener listener);
    void setSeleccionListener(ListSelectionListener listener);
}