package unmdp.fi.programacionc.vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Pantalla de acceso: solo muestra campos y botones. No tiene logica.
 */
public class VentanaLogin extends JFrame implements IVistaLogin {

    private final JTextField campoUsuario = new JTextField(20);
    private final JPasswordField campoContrasenia = new JPasswordField(20);
    private final JButton botonIngresar = new JButton("Ingresar");
    private final JButton botonRegistrarse = new JButton("Registrarse");

    public VentanaLogin() {
        super("Acceso al sistema");
        botonIngresar.setActionCommand(INGRESAR);
        botonRegistrarse.setActionCommand(REGISTRARSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(new JLabel("Usuario:"));
        panel.add(campoUsuario);
        panel.add(new JLabel("Contrasenia:"));
        panel.add(campoContrasenia);
        panel.add(botonIngresar);
        panel.add(botonRegistrarse);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
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

    @Override public void mostrar() { setVisible(true); }
    @Override public void cerrar()  { dispose(); }
}
