package unmdp.fi.programacionc.vista;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Vista de Registro: alta de un usuario nuevo con nombre y contrasenia (pedida dos veces).
 * Misma estetica que el login. No tiene logica.
 */
public class VentanaRegistro extends JFrame implements IVistaRegistro {

    private final JTextField campoUsuario = Tema.campoTexto();
    private final JPasswordField campoContrasenia = Tema.campoContrasenia();
    private final JPasswordField campoConfirmacion = Tema.campoContrasenia();
    private final JButton botonCrear = Tema.botonPrimario("Crear cuenta");
    private final JButton botonVolver = Tema.botonSecundario("Volver");

    public VentanaRegistro() {
        super("Software Embarcado - Registro de usuario");
        botonCrear.setActionCommand(CREAR_CUENTA);
        botonVolver.setActionCommand(VOLVER);

        FondoEspacial fondo = new FondoEspacial(new GridBagLayout());
        fondo.add(crearTarjeta());
        setContentPane(fondo);

        // Cerrar con la X equivale a "Volver": el controlador decide que hacer
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                botonVolver.doClick();
            }
        });
        setSize(820, 600);
        setMinimumSize(new Dimension(520, 540));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(botonCrear);   // Enter = Crear cuenta
    }

    private JPanel crearTarjeta() {
        JPanel tarjeta = Tema.tarjeta();

        JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
        botones.setOpaque(false);
        botones.add(botonCrear);
        botones.add(botonVolver);

        tarjeta.add(Tema.izquierda(Tema.titulo("REGISTRO DE USUARIO")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("Crea tu cuenta para operar la nave")));
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Usuario")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(campoUsuario));
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Contraseña")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(campoContrasenia));
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Repetir contraseña")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(campoConfirmacion));
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(Tema.izquierda(botones));
        return tarjeta;
    }

    @Override public String getUsuario()      { return campoUsuario.getText(); }
    @Override public String getContrasenia()  { return new String(campoContrasenia.getPassword()); }
    @Override public String getConfirmacion() { return new String(campoConfirmacion.getPassword()); }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setActionListener(ActionListener listener) {
        botonCrear.addActionListener(listener);
        botonVolver.addActionListener(listener);
    }

    @Override public void mostrar() { setVisible(true); }
    @Override public void cerrar()  { dispose(); }
}