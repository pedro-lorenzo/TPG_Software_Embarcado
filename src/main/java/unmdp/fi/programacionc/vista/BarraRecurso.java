package unmdp.fi.programacionc.vista;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Barra horizontal para un recurso: nombre arriba a la izquierda, valor a la derecha
 * y una franja que se llena segun el porcentaje. Solo dibuja; el valor se lo da quien la usa.
 */
public class BarraRecurso extends JComponent {

    private static final int ALTO_FRANJA = 8;

    private final String nombre;
    private final int maximo;
    private int valor;
    private Color color = Tema.CIAN;

    /**
     * pre -> nombre no vacio, maximo > 0
     */
    public BarraRecurso(String nombre, int maximo) {
        assert nombre != null && !nombre.isEmpty() && maximo > 0;
        this.nombre = nombre.toUpperCase();
        this.maximo = maximo;
        setPreferredSize(new Dimension(260, 40));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        setAlignmentX(LEFT_ALIGNMENT);
    }

    /** Actualiza el valor y el color (por ejemplo, naranja cuando esta en zona de alerta). */
    public void mostrar(int valor, Color color) {
        this.valor = Math.max(0, Math.min(valor, maximo));
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int ancho = getWidth();
        g2.setFont(Tema.ETIQUETA);
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Tema.TEXTO_SUAVE);
        g2.drawString(nombre, 0, fm.getAscent());
        String texto = valor + " / " + maximo;
        g2.setColor(Tema.TEXTO);
        g2.drawString(texto, ancho - fm.stringWidth(texto), fm.getAscent());

        int y = fm.getHeight() + 8;
        g2.setColor(Tema.CAMPO);
        g2.fillRect(0, y, ancho, ALTO_FRANJA);
        g2.setColor(color);
        g2.fillRect(0, y, ancho * valor / maximo, ALTO_FRANJA);
        g2.dispose();
    }
}
