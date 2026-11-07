package unmdp.fi.programacionc.vista;

import java.awt.event.ActionListener;
import java.util.List;

/**
 * Lo que el controlador puede pedirle a la seccion de Creacion.
 * Solo maneja textos: la vista no conoce las clases del modelo (Nave, Cargo, Origen...).
 */
public interface IVistaCreacion {

    String CREAR_NAVE = "CREAR_NAVE";
    String AGREGAR_TRIPULANTE = "AGREGAR_TRIPULANTE";

    /** Opciones de los desplegables, en el orden en que se muestran. */
    void cargarOpciones(String[] tiposNave, String[] cargos, String[] origenes);

    String getTipoNave();
    String getNombre();
    String getCargo();
    String getOrigen();
    String getAntiguedad();

    void mostrarNave(String descripcion);
    void mostrarTripulantes(List<String> filas);
    /** Estado de la tripulacion: el texto y si ya es valida para operar. */
    void mostrarEstadoTripulacion(String texto, boolean valida);
    void habilitarTripulantes(boolean habilitado);
    void limpiarTripulante();
    void mostrarError(String mensaje);

    void setActionListener(ActionListener listener);
}