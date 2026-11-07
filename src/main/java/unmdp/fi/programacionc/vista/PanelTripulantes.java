package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Seccion de Tripulantes: tabla con todos los tripulantes y su salario; a la derecha,
 * el detalle del salario del tripulante elegido, concepto por concepto (Decorator).
 */
public class PanelTripulantes extends JPanel implements IVistaTripulantes {

    private static final String[] COLUMNAS = {"Nombre", "Cargo", "Origen", "Antiguedad", "Estado", "Salario"};

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0);
    private final JTable tabla = Tema.tabla();
    private final JLabel tituloDetalle = Tema.titulo("SALARIO");
    private final JPanel conceptos = new JPanel();
    private final JLabel total = Tema.titulo("");
    private final JButton botonConsejo = Tema.botonSecundario("Registrar consejo");

    public PanelTripulantes() {
        super(new BorderLayout(16, 0));
        setOpaque(false);
        tabla.setModel(modeloTabla);
        botonConsejo.setActionCommand(REGISTRAR_CONSEJO);

        JPanel izquierda = Tema.tarjeta();
        izquierda.add(Tema.izquierda(Tema.titulo("TRIPULANTES")));
        izquierda.add(Box.createVerticalStrut(4));
        izquierda.add(Tema.izquierda(Tema.subtitulo("Elegi un tripulante para ver el detalle de su salario")));
        izquierda.add(Box.createVerticalStrut(16));
        izquierda.add(Tema.izquierda(Tema.scroll(tabla)));
        add(izquierda, BorderLayout.CENTER);

        JPanel derecha = Tema.tarjeta();
        derecha.setPreferredSize(new Dimension(380, 0));
        conceptos.setOpaque(false);
        conceptos.setLayout(new BoxLayout(conceptos, BoxLayout.Y_AXIS));
        derecha.add(Tema.izquierda(tituloDetalle));
        derecha.add(Box.createVerticalStrut(18));
        derecha.add(Tema.izquierda(conceptos));
        derecha.add(Box.createVerticalStrut(18));
        derecha.add(Tema.izquierda(Tema.etiqueta("Total mensual")));
        derecha.add(Box.createVerticalStrut(4));
        derecha.add(Tema.izquierda(total));
        derecha.add(Box.createVerticalStrut(22));
        derecha.add(Tema.izquierda(botonConsejo));
        derecha.add(Box.createVerticalGlue());
        add(derecha, BorderLayout.EAST);
    }

    // ===================== IVistaTripulantes =====================

    @Override
    public void mostrarTripulantes(List<String[]> filas) {
        modeloTabla.setRowCount(0);
        for (String[] fila : filas) {
            modeloTabla.addRow(fila);
        }
    }

    @Override public int getFilaSeleccionada() { return tabla.getSelectedRow(); }

    @Override
    public void seleccionarFila(int fila) {
        if (fila >= 0 && fila < tabla.getRowCount()) {
            tabla.setRowSelectionInterval(fila, fila);
        }
    }

    @Override
    public void mostrarDetalle(String titulo, List<String[]> lineas, String importeTotal) {
        tituloDetalle.setText(titulo.toUpperCase());
        conceptos.removeAll();
        for (String[] linea : lineas) {
            // cada concepto en su renglon: nombre a la izquierda, importe a la derecha
            JPanel renglon = new JPanel(new BorderLayout(12, 0));
            renglon.setOpaque(false);
            renglon.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE),
                    BorderFactory.createEmptyBorder(8, 0, 8, 0)));
            renglon.add(Tema.subtitulo(linea[0]), BorderLayout.CENTER);
            JLabel importe = Tema.subtitulo(linea[1]);
            importe.setForeground(Tema.TEXTO);
            renglon.add(importe, BorderLayout.EAST);
            renglon.setMaximumSize(new Dimension(Integer.MAX_VALUE, renglon.getPreferredSize().height));
            renglon.setAlignmentX(LEFT_ALIGNMENT);
            conceptos.add(renglon);
        }
        total.setText(importeTotal);
        conceptos.revalidate();
        conceptos.repaint();
    }

    @Override
    public void limpiarDetalle(String mensaje) {
        tituloDetalle.setText("SALARIO");
        conceptos.removeAll();
        conceptos.add(Tema.izquierda(Tema.subtitulo(mensaje)));
        total.setText("");
        conceptos.revalidate();
        conceptos.repaint();
    }

    @Override public void habilitarConsejo(boolean habilitado) { botonConsejo.setEnabled(habilitado); }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override public void setActionListener(ActionListener listener) { botonConsejo.addActionListener(listener); }

    @Override
    public void setSeleccionListener(ListSelectionListener listener) {
        tabla.getSelectionModel().addListSelectionListener(listener);
    }
}
