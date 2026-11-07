package unmdp.fi.programacionc.vista;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Seccion de Mision: a la izquierda se elige y se inicia la mision; a la derecha, el tablero en vivo.
 */
public class PanelMision extends JPanel implements IVistaMision {

    private final JComboBox<String> comboMision = Tema.combo();
    private final JButton botonIniciar = Tema.botonPrimario("Iniciar mision");
    private final JLabel estado = Tema.etiqueta("");
    private final PanelTablero tablero = new PanelTablero();

    public PanelMision() {
        super(new BorderLayout(16, 0));
        setOpaque(false);
        botonIniciar.setActionCommand(INICIAR);
        add(crearTarjetaMision(), BorderLayout.WEST);
        add(crearTarjetaTablero(), BorderLayout.CENTER);
    }

    private JPanel crearTarjetaMision() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.setPreferredSize(new Dimension(340, 0));
        tarjeta.add(Tema.izquierda(Tema.titulo("MISION")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("La nave recorre el tablero turno a turno")));
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Mision")));
        tarjeta.add(Box.createVerticalStrut(6));
        comboMision.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboMision.getPreferredSize().height));
        tarjeta.add(Tema.izquierda(comboMision));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(Tema.izquierda(botonIniciar));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(Tema.izquierda(estado));
        tarjeta.add(Box.createVerticalStrut(28));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Referencias")));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(Tema.izquierda(crearLeyenda()));
        return tarjeta;
    }

    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel(new GridLayout(0, 1, 0, 6));
        leyenda.setOpaque(false);
        leyenda.add(referencia("◆", Tema.CIAN, "Tu nave"));
        leyenda.add(referencia("A", PanelTablero.colorDe('A'), "Asteroide"));
        leyenda.add(referencia("E", PanelTablero.colorDe('E'), "Estacion de carga"));
        leyenda.add(referencia("X C K", PanelTablero.colorDe('x'), "Naves enemigas"));
        leyenda.setMaximumSize(new Dimension(Integer.MAX_VALUE, leyenda.getPreferredSize().height));
        return leyenda;
    }

    private JPanel referencia(String simbolo, java.awt.Color color, String texto) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        JLabel s = Tema.subtitulo(simbolo);
        s.setForeground(color);
        s.setPreferredSize(new Dimension(48, s.getPreferredSize().height));
        fila.add(s, BorderLayout.WEST);
        fila.add(Tema.subtitulo(texto), BorderLayout.CENTER);
        return fila;
    }

    private JPanel crearTarjetaTablero() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(Tema.titulo("TABLERO EN VIVO")));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(Tema.izquierda(tablero));
        return tarjeta;
    }

    // ===================== IVistaMision =====================

    @Override
    public void cargarMisiones(String[] nombres) {
        for (String n : nombres) comboMision.addItem(n);
    }

    @Override public int getMisionElegida() { return comboMision.getSelectedIndex(); }

    @Override public void mostrarTablero(char[][] celdas, int xNave, int yNave) { tablero.mostrar(celdas, xNave, yNave); }
    @Override public void limpiarTablero() { tablero.limpiar(); }

    @Override
    public void mostrarEstado(String texto, boolean alerta) {
        estado.setText(texto.toUpperCase());
        estado.setForeground(alerta ? Tema.NARANJA : Tema.CIAN);
    }

    @Override
    public void habilitarInicio(boolean habilitado) {
        comboMision.setEnabled(habilitado);
        botonIniciar.setEnabled(habilitado);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "No se puede iniciar", JOptionPane.ERROR_MESSAGE);
    }

    @Override public void setActionListener(ActionListener listener) { botonIniciar.addActionListener(listener); }
}