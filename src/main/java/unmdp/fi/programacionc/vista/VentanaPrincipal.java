package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Ventana principal: barra superior con el nombre del sistema y el operador, una fila de
 * pestanias y, debajo, la seccion elegida (se muestra una por vez con CardLayout).
 *
 * Cambiar de pestania es comportamiento puramente visual (no toca el modelo), por eso lo
 * resuelve la propia vista. Que hay dentro de cada seccion lo decide quien la agrega.
 */
public class VentanaPrincipal extends JFrame {

    private final JPanel filaPestanias = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final ButtonGroup grupo = new ButtonGroup();

    public VentanaPrincipal(String usuario) {
        super("Software Embarcado - Panel de control");

        FondoEspacial fondo = new FondoEspacial(new BorderLayout());
        fondo.add(crearEncabezado(usuario), BorderLayout.NORTH);

        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        fondo.add(contenido, BorderLayout.CENTER);

        setContentPane(fondo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }

    /**
     * Agrega una pestania con su seccion. La primera que se agrega queda seleccionada.
     * pre -> nombre no vacio y seccion != null
     */
    public void agregarSeccion(String nombre, JComponent seccion) {
        assert nombre != null && !nombre.trim().isEmpty() : "La seccion necesita un nombre";
        assert seccion != null : "La seccion no puede ser nula";
        JToggleButton pestania = Tema.botonPestania(nombre);
        pestania.addActionListener(e -> seleccionar(pestania, nombre));
        grupo.add(pestania);
        filaPestanias.add(pestania);
        contenido.add(seccion, nombre);
        if (grupo.getButtonCount() == 1) {
            seleccionar(pestania, nombre);
        }
    }

    public void mostrar() { setVisible(true); }

    private void seleccionar(JToggleButton pestania, String nombre) {
        for (java.util.Enumeration<javax.swing.AbstractButton> e = grupo.getElements(); e.hasMoreElements(); ) {
            javax.swing.AbstractButton otra = e.nextElement();
            otra.setSelected(otra == pestania);
        }
        tarjetas.show(contenido, nombre);
    }

    private JPanel crearEncabezado(String usuario) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Tema.FONDO);
        barra.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        barra.add(Tema.titulo("SOFTWARE EMBARCADO"), BorderLayout.WEST);
        JLabel operador = Tema.subtitulo("Operador: " + usuario);
        barra.add(operador, BorderLayout.EAST);

        filaPestanias.setBackground(Tema.PANEL);
        filaPestanias.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE));

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(barra);
        encabezado.add(filaPestanias);
        return encabezado;
    }
}
