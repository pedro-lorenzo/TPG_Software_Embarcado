package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
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
        // al pasar el mouse se aclara un poco
        Color normal = boton.getBackground();
        Color encima = relleno ? color.brighter() : BORDE;
        boton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { boton.setBackground(encima); }
            @Override public void mouseExited(MouseEvent e)  { boton.setBackground(normal); }
        });
        return boton;
    }

    /** Para alinear a la izquierda dentro de un BoxLayout vertical. */
    public static <T extends JComponent> T izquierda(T componente) {
        componente.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return componente;
    }
}