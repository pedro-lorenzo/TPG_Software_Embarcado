package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Seccion de Control de la nave. Columna izquierda: estado y recursos, y debajo las ordenes
 * (cargas y mantenimiento). A la derecha, la bitacora completa, como una consola.
 */
public class PanelControl extends JPanel implements IVistaControl {

    private final JLabel tituloNave = Tema.titulo("SIN NAVE");
    private final JLabel estadoMotor = Tema.subtitulo("");
    private final JLabel estadoGeneral = Tema.etiqueta("");
    private final BarraRecurso barraCombustible = new BarraRecurso("Combustible", 100);
    private final BarraRecurso barraEnergia = new BarraRecurso("Energia", 100);
    private final BarraRecurso barraDesgaste = new BarraRecurso("Desgaste", 100);
    private final JLabel gemas = Tema.subtitulo("");

    private final JTextField campoCantidad = Tema.campoTexto();
    private final JButton botonCombustible = Tema.botonPrimario("+ Combustible");
    private final JButton botonEnergia = Tema.botonPrimario("+ Energia");
    private final JButton botonMantenimiento = Tema.botonSecundario("Realizar mantenimiento");

    private final JTextArea bitacora = new JTextArea();

    public PanelControl() {
        super(new BorderLayout(16, 0));
        setOpaque(false);
        botonCombustible.setActionCommand(CARGAR_COMBUSTIBLE);
        botonEnergia.setActionCommand(CARGAR_ENERGIA);
        botonMantenimiento.setActionCommand(MANTENIMIENTO);

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.setPreferredSize(new Dimension(380, 0));
        izquierda.add(crearTarjetaEstado());
        izquierda.add(Box.createVerticalStrut(16));
        izquierda.add(crearTarjetaOrdenes());
        add(izquierda, BorderLayout.WEST);
        add(crearTarjetaBitacora(), BorderLayout.CENTER);
    }

    private JPanel crearTarjetaEstado() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(tituloNave));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(estadoMotor));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(estadoGeneral));
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(barraCombustible);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(barraEnergia);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(barraDesgaste);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(Tema.izquierda(gemas));
        return tarjeta;
    }

    private JPanel crearTarjetaOrdenes() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(Tema.titulo("ORDENES")));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Cantidad a cargar")));
        tarjeta.add(Box.createVerticalStrut(6));
        campoCantidad.setMaximumSize(new Dimension(Integer.MAX_VALUE, campoCantidad.getPreferredSize().height));
        tarjeta.add(Tema.izquierda(campoCantidad));
        tarjeta.add(Box.createVerticalStrut(10));
        JPanel cargas = new JPanel(new GridLayout(1, 2, 10, 0));
        cargas.setOpaque(false);
        cargas.add(botonCombustible);
        cargas.add(botonEnergia);
        tarjeta.add(Tema.izquierda(cargas));
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(Tema.izquierda(botonMantenimiento));
        return tarjeta;
    }

    private JPanel crearTarjetaBitacora() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(Tema.titulo("BITACORA")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("Todo lo que le paso a la nave, en orden")));
        tarjeta.add(Box.createVerticalStrut(14));
        bitacora.setEditable(false);
        bitacora.setFont(Tema.CONSOLA);
        bitacora.setBackground(Tema.CAMPO);
        bitacora.setForeground(Tema.TEXTO);
        bitacora.setLineWrap(true);
        bitacora.setWrapStyleWord(true);
        bitacora.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane scroll = Tema.scroll(bitacora);
        tarjeta.add(Tema.izquierda(scroll));
        return tarjeta;
    }

    // ===================== IVistaControl =====================

    @Override
    public void mostrarNave(String titulo, String motor, String general, boolean alerta) {
        tituloNave.setText(titulo.toUpperCase());
        estadoMotor.setText("Motor: " + motor);
        estadoGeneral.setText(general.toUpperCase());
        estadoGeneral.setForeground(alerta ? Tema.NARANJA : Tema.CIAN);
    }

    @Override
    public void mostrarRecursos(int combustible, int energia, int desgaste, boolean desgasteEnAlerta, int cantidadGemas) {
        barraCombustible.mostrar(combustible, Tema.CIAN);
        barraEnergia.mostrar(energia, Tema.CIAN);
        barraDesgaste.mostrar(desgaste, desgasteEnAlerta ? Tema.NARANJA : Tema.TEXTO_SUAVE);
        gemas.setText("Gemas a bordo: " + cantidadGemas);
    }

    @Override
    public void mostrarBitacora(List<String> lineas) {
        bitacora.setText(String.join("\n", lineas));
        bitacora.setCaretPosition(bitacora.getDocument().getLength());   // siempre lo ultimo a la vista
    }

    @Override
    public void sinNave() {
        mostrarNave("Sin nave", "-", "Crea una nave en la seccion Creacion", true);
        mostrarRecursos(0, 0, 0, false, 0);
        bitacora.setText("");
    }

    @Override public String getCantidad() { return campoCantidad.getText(); }
    @Override public void limpiarCantidad() { campoCantidad.setText(""); }

    @Override
    public void habilitarAcciones(boolean habilitado) {
        campoCantidad.setEnabled(habilitado);
        botonCombustible.setEnabled(habilitado);
        botonEnergia.setEnabled(habilitado);
        botonMantenimiento.setEnabled(habilitado);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Orden rechazada", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setActionListener(ActionListener listener) {
        botonCombustible.addActionListener(listener);
        botonEnergia.addActionListener(listener);
        botonMantenimiento.addActionListener(listener);
    }
}
