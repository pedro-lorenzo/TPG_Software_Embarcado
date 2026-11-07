package unmdp.fi.programacionc.vista;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Seccion de Creacion: a la izquierda se elige y crea la nave; a la derecha se cargan
 * tripulantes y se ve la tripulacion asignada. Solo muestra; no tiene logica.
 */
public class PanelCreacion extends JPanel implements IVistaCreacion {

    private final JComboBox<String> comboTipo = Tema.combo();
    private final JButton botonCrearNave = Tema.botonPrimario("Crear nave");
    private final JLabel etiquetaNave = Tema.subtitulo("Todavia no hay una nave en uso");

    private final JTextField campoNombre = Tema.campoTexto();
    private final JComboBox<String> comboCargo = Tema.combo();
    private final JComboBox<String> comboOrigen = Tema.combo();
    private final JTextField campoAntiguedad = Tema.campoTexto();
    private final JButton botonAgregar = Tema.botonPrimario("Agregar tripulante");

    private final JList<String> listaTripulantes = Tema.lista();
    private final JLabel etiquetaEstado = Tema.etiqueta("");

    public PanelCreacion() {
        super(new GridLayout(1, 2, 16, 0));
        setOpaque(false);
        botonCrearNave.setActionCommand(CREAR_NAVE);
        botonAgregar.setActionCommand(AGREGAR_TRIPULANTE);
        add(crearTarjetaNave());
        add(crearTarjetaTripulacion());
    }

    private JPanel crearTarjetaNave() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(Tema.titulo("NAVE")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("Elegi el tipo de nave que vas a operar")));
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Tipo de nave")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(acotar(comboTipo)));
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(Tema.izquierda(botonCrearNave));
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Nave en uso")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(etiquetaNave));
        tarjeta.add(Box.createVerticalGlue());
        return tarjeta;
    }

    private JPanel crearTarjetaTripulacion() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(Tema.titulo("TRIPULACION")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("1 capitan y al menos 4 tripulantes mas")));
        tarjeta.add(Box.createVerticalStrut(20));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 8));
        formulario.setOpaque(false);
        formulario.add(Tema.etiqueta("Nombre"));
        formulario.add(campoNombre);
        formulario.add(Tema.etiqueta("Cargo"));
        formulario.add(comboCargo);
        formulario.add(Tema.etiqueta("Origen"));
        formulario.add(comboOrigen);
        formulario.add(Tema.etiqueta("Antiguedad (anios)"));
        formulario.add(campoAntiguedad);
        tarjeta.add(Tema.izquierda(formulario));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(Tema.izquierda(botonAgregar));
        tarjeta.add(Box.createVerticalStrut(20));

        tarjeta.add(Tema.izquierda(etiquetaEstado));
        tarjeta.add(Box.createVerticalStrut(8));
        JScrollPane scroll = new JScrollPane(listaTripulantes);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE, 1));
        scroll.getViewport().setBackground(Tema.CAMPO);
        tarjeta.add(Tema.izquierda(scroll));
        return tarjeta;
    }

    // Los combos en un BoxLayout se estiran a lo alto: se les fija la altura
    private static <T extends javax.swing.JComponent> T acotar(T componente) {
        componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, componente.getPreferredSize().height));
        return componente;
    }

    // ===================== IVistaCreacion =====================

    @Override
    public void cargarOpciones(String[] tiposNave, String[] cargos, String[] origenes) {
        for (String t : tiposNave) comboTipo.addItem(t);
        for (String c : cargos) comboCargo.addItem(c);
        for (String o : origenes) comboOrigen.addItem(o);
    }

    @Override public String getTipoNave()   { return (String) comboTipo.getSelectedItem(); }
    @Override public String getNombre()     { return campoNombre.getText(); }
    @Override public String getCargo()      { return (String) comboCargo.getSelectedItem(); }
    @Override public String getOrigen()     { return (String) comboOrigen.getSelectedItem(); }
    @Override public String getAntiguedad() { return campoAntiguedad.getText(); }

    @Override public void mostrarNave(String descripcion) { etiquetaNave.setText(descripcion); }

    @Override
    public void mostrarTripulantes(List<String> filas) {
        listaTripulantes.setListData(filas.toArray(new String[0]));
    }

    @Override
    public void mostrarEstadoTripulacion(String texto, boolean valida) {
        etiquetaEstado.setText(texto.toUpperCase());
        etiquetaEstado.setForeground(valida ? Tema.CIAN : Tema.NARANJA);
    }

    @Override
    public void habilitarTripulantes(boolean habilitado) {
        campoNombre.setEnabled(habilitado);
        comboCargo.setEnabled(habilitado);
        comboOrigen.setEnabled(habilitado);
        campoAntiguedad.setEnabled(habilitado);
        botonAgregar.setEnabled(habilitado);
    }

    @Override
    public void limpiarTripulante() {
        campoNombre.setText("");
        campoAntiguedad.setText("");
        campoNombre.requestFocusInWindow();
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setActionListener(ActionListener listener) {
        botonCrearNave.addActionListener(listener);
        botonAgregar.addActionListener(listener);
    }
}
