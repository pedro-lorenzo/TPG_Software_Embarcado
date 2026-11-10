package unmdp.fi.programacionc.vista;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Dibuja el tablero de la mision: una grilla con un simbolo por casilla y la nave propia resaltada.
 * Solo dibuja lo ultimo que le pasaron; no sabe nada del modelo.
 */
public class PanelTablero extends JComponent {

    private char[][] celdas = new char[0][0];
    private int xNave = -1;
    private int yNave = -1;

    public PanelTablero() {
        setPreferredSize(new Dimension(520, 520));
    }

    /** Guarda una copia de la foto del tablero y lo vuelve a dibujar. */
    public void mostrar(char[][] celdas, int xNave, int yNave) {
        char[][] copia = new char[celdas.length][];
        for (int i = 0; i < celdas.length; i++) {
            copia[i] = celdas[i].clone();
        }
        this.celdas = copia;
        this.xNave = xNave;
        this.yNave = yNave;
        repaint();
    }

    public void limpiar() {
        mostrar(new char[0][0], -1, -1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Tema.CAMPO);
        g2.fillRect(0, 0, getWidth(), getHeight());
        int columnas = celdas.length;
        int filas = columnas == 0 ? 0 : celdas[0].length;
        if (columnas == 0 || filas == 0) {
            g2.dispose();
            return;
        }
        int lado = Math.max(4, Math.min(getWidth() / columnas, getHeight() / filas));
        int x0 = (getWidth() - lado * columnas) / 2;
        int y0 = (getHeight() - lado * filas) / 2;

        // grilla
        g2.setColor(Tema.BORDE);
        for (int i = 0; i <= columnas; i++) {
            g2.drawLine(x0 + i * lado, y0, x0 + i * lado, y0 + filas * lado);
        }
        for (int j = 0; j <= filas; j++) {
            g2.drawLine(x0, y0 + j * lado, x0 + columnas * lado, y0 + j * lado);
        }

        // elementos
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(9, lado / 2)));
        FontMetrics fm = g2.getFontMetrics();
        for (int x = 0; x < columnas; x++) {
            for (int y = 0; y < filas; y++) {
                char simbolo = celdas[x][y];
                if (simbolo != ' ' && simbolo != 0) {
                    dibujarElemento(g2, fm, simbolo, x0 + x * lado, y0 + y * lado, lado);
                }
            }
        }

        // nave propia
        if (xNave >= 0 && yNave >= 0) {
            int cx = x0 + xNave * lado, cy = y0 + yNave * lado;
            g2.setColor(new Color(Tema.CIAN.getRed(), Tema.CIAN.getGreen(), Tema.CIAN.getBlue(), 60));
            g2.fillRect(cx + 1, cy + 1, lado - 1, lado - 1);
            g2.setColor(Tema.CIAN);
            int m = lado / 5;
            int[] px = {cx + lado / 2, cx + lado - m, cx + lado / 2, cx + m};
            int[] py = {cy + m, cy + lado / 2, cy + lado - m, cy + lado / 2};
            g2.fillPolygon(px, py, 4);
        }
        g2.dispose();
    }

    private void dibujarElemento(Graphics2D g2, FontMetrics fm, char simbolo, int cx, int cy, int lado) {
        Color color = colorDe(simbolo);
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 45));
        g2.fillRect(cx + 1, cy + 1, lado - 1, lado - 1);
        g2.setColor(color);
        String texto = String.valueOf(Character.toUpperCase(simbolo));
        g2.drawString(texto, cx + (lado - fm.stringWidth(texto)) / 2, cy + (lado + fm.getAscent() - fm.getDescent()) / 2);
    }

    /** A asteroide, E estacion de carga, x/c/k naves enemigas (exploradora, carguero, combate). */
    static Color colorDe(char simbolo) {
        switch (simbolo) {
            case 'A': return Tema.TEXTO_SUAVE;
            case 'E': return Tema.CIAN;
            case 'x':
            case 'c':
            case 'k': return Tema.ERROR;
            default:  return Tema.NARANJA;
        }
    }
}