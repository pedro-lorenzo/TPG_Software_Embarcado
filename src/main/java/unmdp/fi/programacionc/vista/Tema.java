package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Estetica comun de todas las pantallas: colores, fuentes y componentes ya estilizados.
 * Es solo presentacion (paquete vista): ninguna clase del modelo ni del controlador la usa.
 * Cambiando un color aca, cambia en todo el sistema.
 */
public final class Tema {

    // ---- paleta: panel de control espacial, fondo azul noche con acentos cian y naranja ----
    public static final Color FONDO        = new Color(0x0A1520);
    public static final Color PANEL        = new Color(0x0F2131);
    public static final Color CAMPO        = new Color(0x0B1A27);
    public static final Color BORDE        = new Color(0x24445E);
    public static final Color CIAN         = new Color(0x3FD0E6);
    public static final Color NARANJA      = new Color(0xF2A33A);
    public static final Color TEXTO        = new Color(0xDCE9F4);
    public static final Color TEXTO_SUAVE  = new Color(0x7F99AF);
    public static final Color ERROR        = new Color(0xF0605D);

    // ---- tipografia ----
    public static final Font TITULO   = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    public static final Font ETIQUETA = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    public static final Font NORMAL   = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font BOTON    = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    public static final Font CONSOLA  = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    private Tema() { }

    /** Colores de los cuadros de dialogo (JOptionPane). Llamar una vez al arrancar la interfaz. */
    public static void aplicarGlobal() {
        UIManager.put("OptionPane.background", PANEL);
        UIManager.put("Panel.background", PANEL);
        UIManager.put("OptionPane.messageForeground", TEXTO);
        UIManager.put("Button.background", CIAN);
        UIManager.put("Button.foreground", FONDO);
        UIManager.put("ComboBox.background", CAMPO);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("ComboBox.selectionBackground", BORDE);
        UIManager.put("ComboBox.selectionForeground", TEXTO);
        UIManager.put("ComboBox.buttonBackground", CAMPO);
        UIManager.put("ComboBox.buttonDarkShadow", TEXTO_SUAVE);
        UIManager.put("ComboBox.disabledBackground", CAMPO);
        UIManager.put("ComboBox.disabledForeground", TEXTO_SUAVE);
        UIManager.put("Button.disabledText", TEXTO_SUAVE);
        // barras de desplazamiento oscuras
        UIManager.put("ScrollBar.background", CAMPO);
        UIManager.put("ScrollBar.track", CAMPO);
        UIManager.put("ScrollBar.thumb", BORDE);
        UIManager.put("ScrollBar.thumbHighlight", BORDE);
        UIManager.put("ScrollBar.thumbShadow", BORDE);
        UIManager.put("ScrollBar.thumbDarkShadow", BORDE);
        UIManager.put("ScrollBar.shadow", CAMPO);
        UIManager.put("ScrollBar.darkShadow", CAMPO);
        UIManager.put("ScrollBar.highlight", CAMPO);
        UIManager.put("ScrollBar.width", 10);
    }

    public static JLabel titulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(TITULO);
        etiqueta.setForeground(CIAN);
        return etiqueta;
    }

    public static JLabel subtitulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(NORMAL);
        etiqueta.setForeground(TEXTO_SUAVE);
        return etiqueta;
    }

    /** Rotulo chico en mayusculas, arriba de cada campo. */
    public static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto.toUpperCase());
        etiqueta.setFont(ETIQUETA);
        etiqueta.setForeground(TEXTO_SUAVE);
        return etiqueta;
    }

    public static JTextField campoTexto() {
        return estilizarCampo(new JTextField(20));
    }

    public static JPasswordField campoContrasenia() {
        return estilizarCampo(new JPasswordField(20));
    }

    /** Boton principal: relleno cian. */
    public static JButton botonPrimario(String texto) {
        return boton(texto, CIAN, FONDO, true);
    }

    /** Boton secundario: solo el borde, en naranja. */
    public static JButton botonSecundario(String texto) {
        return boton(texto, NARANJA, NARANJA, false);
    }

    /** Boton de la fila de pestanias. Se marca con setSelected(true) en la pestania activa. */
    public static JToggleButton botonPestania(String texto) {
        JToggleButton boton = new JToggleButton(texto.toUpperCase()) {
            @Override public void setSelected(boolean seleccionado) {
                super.setSelected(seleccionado);
                setForeground(seleccionado ? CIAN : TEXTO_SUAVE);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, seleccionado ? CIAN : PANEL),
                        BorderFactory.createEmptyBorder(12, 20, 10, 20)));
            }
        };
        boton.setFont(BOTON);
        boton.setBackground(PANEL);
        boton.setOpaque(true);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setSelected(false);
        return boton;
    }

    /** Tarjeta vertical: fondo de panel, franja cian arriba. Se le agregan componentes con Tema.izquierda(...). */
    public static JPanel tarjeta() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(PANEL);
        tarjeta.setBorder(bordePanel());
        return tarjeta;
    }

    public static JComboBox<String> combo() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(NORMAL);
        combo.setBackground(CAMPO);
        combo.setForeground(TEXTO);
        combo.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        combo.setFocusable(false);
        // cada opcion (y la elegida) en colores oscuros, sin el recuadro blanco de Swing
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, false);
                setBackground(isSelected && index >= 0 ? BORDE : CAMPO);
                setForeground(combo.isEnabled() ? TEXTO : TEXTO_SUAVE);
                setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                return this;
            }
        });
        return combo;
    }

    public static JList<String> lista() {
        JList<String> lista = new JList<>();
        lista.setFont(NORMAL);
        lista.setBackground(CAMPO);
        lista.setForeground(TEXTO);
        lista.setSelectionBackground(BORDE);
        lista.setSelectionForeground(TEXTO);
        lista.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return lista;
    }

    /** Tabla oscura de solo lectura: se elige una fila por vez. */
    public static JTable tabla() {
        JTable tabla = new JTable() {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla.setFont(NORMAL);
        tabla.setRowHeight(28);
        tabla.setBackground(CAMPO);
        tabla.setForeground(TEXTO);
        tabla.setGridColor(BORDE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(BORDE);
        tabla.setSelectionForeground(TEXTO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setReorderingAllowed(false);
        DefaultTableCellRenderer estiloEncabezado = new DefaultTableCellRenderer();
        estiloEncabezado.setBackground(PANEL);
        estiloEncabezado.setForeground(TEXTO_SUAVE);
        estiloEncabezado.setFont(ETIQUETA);
        estiloEncabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                BorderFactory.createEmptyBorder(8, 6, 8, 6)));
        encabezado.setDefaultRenderer(estiloEncabezado);
        return tabla;
    }

    /** Scroll con borde fino y fondo oscuro, para tablas y listas. */
    public static JScrollPane scroll(java.awt.Component contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        scroll.getViewport().setBackground(CAMPO);
        return scroll;
    }

    /** Borde de las tarjetas y paneles: franja cian arriba, linea fina alrededor y aire adentro. */
    public static Border bordePanel() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(2, 0, 0, 0, CIAN),
                        BorderFactory.createMatteBorder(0, 1, 1, 1, BORDE)),
                BorderFactory.createEmptyBorder(28, 32, 28, 32));
    }

    // ===================== auxiliares =====================

    private static <T extends JTextField> T estilizarCampo(T campo) {
        campo.setFont(NORMAL);
        campo.setBackground(CAMPO);
        campo.setForeground(TEXTO);
        campo.setCaretColor(CIAN);
        campo.setBorder(bordeCampo(BORDE));
        // el borde se pone cian mientras el campo tiene el foco
        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { campo.setBorder(bordeCampo(CIAN)); }
            @Override public void focusLost(FocusEvent e)   { campo.setBorder(bordeCampo(BORDE)); }
        });
        return campo;
    }

    private static Border bordeCampo(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    private static JButton boton(String texto, Color color, Color colorTexto, boolean relleno) {
        JButton boton = new JButton(texto.toUpperCase());
        boton.setFont(BOTON);
        boton.setForeground(colorTexto);
        boton.setBackground(relleno ? color : PANEL);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // deshabilitado se ve apagado; habilitado vuelve a su color
        boton.addPropertyChangeListener("enabled", e -> {
            boolean activo = boton.isEnabled();
            boton.setBackground(activo ? (relleno ? color : PANEL) : PANEL);
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(activo ? color : BORDE, 1),
                    BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        });
        // al pasar el mouse se aclara un poco
        Color normal = boton.getBackground();
        Color encima = relleno ? color.brighter() : BORDE;
        boton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { if (boton.isEnabled()) boton.setBackground(encima); }
            @Override public void mouseExited(MouseEvent e)  { if (boton.isEnabled()) boton.setBackground(normal); }
        });
        return boton;
    }

    /** Para alinear a la izquierda dentro de un BoxLayout vertical. */
    public static <T extends JComponent> T izquierda(T componente) {
        componente.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return componente;
    }
}