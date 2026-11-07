package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Visor del universo paralelo: a la izquierda los dos botones para intervenir y cuantas
 * entidades hay; a la derecha la consola con el flujo de eventos en tiempo real.
 */
public class PanelVisor extends JPanel implements IVistaVisor {

    /** La consola guarda las ultimas lineas: la simulacion no termina nunca y no puede crecer sin limite. */
    private static final int MAXIMO_LINEAS = 400;

    private final JButton botonGeneradora = Tema.botonPrimario("Crear generadora de energia");
    private final JButton botonNave = Tema.botonSecundario("Crear nave consumidora");
    private final JLabel contadorGeneradoras = Tema.titulo("0");
    private final JLabel contadorNaves = Tema.titulo("0");
    private final JTextArea consola = new JTextArea();

    public PanelVisor() {
        super(new BorderLayout(16, 0));
        setOpaque(false);
        botonGeneradora.setActionCommand(CREAR_GENERADORA);
        botonNave.setActionCommand(CREAR_NAVE);
        add(crearTarjetaControl(), BorderLayout.WEST);
        add(crearTarjetaConsola(), BorderLayout.CENTER);
    }

    private JPanel crearTarjetaControl() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.setPreferredSize(new Dimension(340, 0));
        tarjeta.add(Tema.izquierda(Tema.titulo("UNIVERSO PARALELO")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("Plataforma de carga con capacidad 3")));
        tarjeta.add(Box.createVerticalStrut(24));

        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 12));
        botones.setOpaque(false);
        botones.add(botonGeneradora);
        botones.add(botonNave);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, botones.getPreferredSize().height));
        tarjeta.add(Tema.izquierda(botones));
        tarjeta.add(Box.createVerticalStrut(28));

        JPanel contadores = new JPanel(new GridLayout(2, 2, 12, 4));
        contadores.setOpaque(false);
        contadores.add(Tema.etiqueta("Generadoras"));
        contadores.add(Tema.etiqueta("Naves"));
        contadorNaves.setForeground(Tema.NARANJA);
        contadores.add(contadorGeneradoras);
        contadores.add(contadorNaves);
        contadores.setMaximumSize(new Dimension(Integer.MAX_VALUE, contadores.getPreferredSize().height));
        tarjeta.add(Tema.izquierda(contadores));
        return tarjeta;
    }

    private JPanel crearTarjetaConsola() {
        JPanel tarjeta = Tema.tarjeta();
        tarjeta.add(Tema.izquierda(Tema.titulo("FLUJO DE EVENTOS")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("En tiempo real")));
        tarjeta.add(Box.createVerticalStrut(14));
        consola.setEditable(false);
        consola.setFont(Tema.CONSOLA);
        consola.setBackground(Tema.CAMPO);
        consola.setForeground(Tema.TEXTO);
        consola.setLineWrap(true);
        consola.setWrapStyleWord(true);
        consola.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        tarjeta.add(Tema.izquierda(Tema.scroll(consola)));
        return tarjeta;
    }

    // ===================== IVistaVisor =====================

    @Override
    public void mostrarContadores(int generadoras, int naves) {
        contadorGeneradoras.setText(String.valueOf(generadoras));
        contadorNaves.setText(String.valueOf(naves));
    }

    @Override
    public void agregarEvento(String linea) {
        consola.append(linea + "\n");
        recortar();
        consola.setCaretPosition(consola.getDocument().getLength());   // siempre lo ultimo a la vista
    }

    private void recortar() {
        Element raiz = consola.getDocument().getDefaultRootElement();
        int sobrantes = raiz.getElementCount() - MAXIMO_LINEAS;
        if (sobrantes > 0) {
            try {
                consola.getDocument().remove(0, raiz.getElement(sobrantes - 1).getEndOffset());
            } catch (BadLocationException e) {
                consola.setText("");
            }
        }
    }

    @Override
    public void setActionListener(ActionListener listener) {
        botonGeneradora.addActionListener(listener);
        botonNave.addActionListener(listener);
    }
}
