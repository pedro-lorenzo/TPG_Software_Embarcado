package unmdp.fi.programacionc.vista;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.Box;

/**
 * Seccion provisoria mientras se construye la verdadera.
 */
public class PanelEnConstruccion extends JPanel {

    public PanelEnConstruccion(String seccion) {
        super(new GridBagLayout());
        setOpaque(false);
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Tema.PANEL);
        tarjeta.setBorder(Tema.bordePanel());
        tarjeta.add(Tema.izquierda(Tema.titulo(seccion.toUpperCase())));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("Seccion en construccion")));
        add(tarjeta);
    }
}
