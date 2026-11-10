package unmdp.fi.programacionc.vista;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

/**
 * Fondo de las ventanas: degradado azul noche con una grilla tenue, como una pantalla de radar.
 * Es un JPanel comun: se le agregan componentes encima.
 */
public class FondoEspacial extends JPanel {

    private static final int PASO_GRILLA = 40;
    private static final Color GRILLA = new Color(0x3FD0E6 & 0xFFFFFF | 0x12000000, true);

    public FondoEspacial(LayoutManager layout) {
        super(layout);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int ancho = getWidth(), alto = getHeight();
        g2.setPaint(new GradientPaint(0, 0, new Color(0x0E1E2D), 0, alto, Tema.FONDO));
        g2.fillRect(0, 0, ancho, alto);
        g2.setColor(GRILLA);
        for (int x = 0; x < ancho; x += PASO_GRILLA) {
            g2.drawLine(x, 0, x, alto);
        }
        for (int y = 0; y < alto; y += PASO_GRILLA) {
            g2.drawLine(0, y, ancho, y);
        }
        g2.dispose();
    }
}
