package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;
import java.util.List;

/**
 * Lo que el controlador puede pedirle a la seccion de Control de la nave.
 * Recibe textos y numeros sueltos, nunca objetos del modelo.
 */
public interface IVistaControl {

    String CARGAR_COMBUSTIBLE = "CARGAR_COMBUSTIBLE";
    String CARGAR_ENERGIA = "CARGAR_ENERGIA";
    String MANTENIMIENTO = "MANTENIMIENTO";

    /** Encabezado: que nave es, estado del motor y una linea de estado general (alerta = se resalta). */
    void mostrarNave(String titulo, String estadoMotor, String estadoGeneral, boolean alerta);
    void mostrarRecursos(int combustible, int energia, int desgaste, boolean desgasteEnAlerta, int gemas);
    void mostrarBitacora(List<String> lineas);
    void mostrarInformes(List<String> lineas);
    void sinNave();

    String getCantidad();
    void limpiarCantidad();
    void habilitarAcciones(boolean habilitado);
    void mostrarError(String mensaje);

    void setActionListener(ActionListener listener);
}
