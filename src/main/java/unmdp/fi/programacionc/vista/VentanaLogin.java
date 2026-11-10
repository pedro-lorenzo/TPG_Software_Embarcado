package unmdp.fi.programacionc.vista;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Pantalla de acceso: una tarjeta centrada sobre el fondo espacial.
 * Solo muestra campos y botones. No tiene logica: todo lo decide el controlador.
 */
public class VentanaLogin extends JFrame implements IVistaLogin {

    private final JTextField campoUsuario = Tema.campoTexto();
    private final JPasswordField campoContrasenia = Tema.campoContrasenia();
    private final JButton botonIngresar = Tema.botonPrimario("Ingresar");
    private final JButton botonRegistrarse = Tema.botonSecundario("Crear cuenta");

    public VentanaLogin() {
        super("Software Embarcado - Acceso al sistema");
        botonIngresar.setActionCommand(INGRESAR);
        botonRegistrarse.setActionCommand(IR_A_REGISTRO);

        // Fondo que ocupa toda la ventana; la tarjeta queda centrada (GridBagLayout sin restricciones)
        FondoEspacial fondo = new FondoEspacial(new GridBagLayout());
        fondo.add(crearTarjeta());

        setContentPane(fondo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 560);
        setMinimumSize(new Dimension(520, 480));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(botonIngresar);   // Enter = Ingresar
    }

    private JPanel crearTarjeta() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Tema.PANEL);
        tarjeta.setBorder(Tema.bordePanel());

        JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
        botones.setOpaque(false);
        botones.add(botonIngresar);
        botones.add(botonRegistrarse);

        tarjeta.add(Tema.izquierda(Tema.titulo("ACCESO AL SISTEMA")));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(Tema.izquierda(Tema.subtitulo("Identificate para tomar el mando de la nave")));
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Usuario")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(campoUsuario));
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(Tema.izquierda(Tema.etiqueta("Contraseña")));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(Tema.izquierda(campoContrasenia));
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(Tema.izquierda(botones));
        return tarjeta;
    }

    @Override public String getUsuario()     { return campoUsuario.getText(); }
    @Override public String getContrasenia() { return new String(campoContrasenia.getPassword()); }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Acceso", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void limpiar() {
        campoUsuario.setText("");
        campoContrasenia.setText("");
    }

    @Override
    public void setActionListener(ActionListener listener) {
        botonIngresar.addActionListener(listener);
        botonRegistrarse.addActionListener(listener);
    }

    @Override
    public void setUsuario(String usuario) {
        campoUsuario.setText(usuario);
        campoContrasenia.setText("");
        campoContrasenia.requestFocusInWindow();
    }

    @Override public void mostrar() { setVisible(true); }
    @Override public void ocultar() { setVisible(false); }
    @Override public void cerrar()  { dispose(); }
}