package projectInfo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog {

    private final JTextField txtUsuario;
    private final JPasswordField txtPassword;
    private int intentos = 0;
    private final BibliotecaData data;

    public LoginDialog(Frame owner, BibliotecaData data) {
        super(owner, "Acceso Biblioteca", true); // modal
        this.data = data;

        txtUsuario = new JTextField(15);
        txtPassword = new JPasswordField(15);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.setToolTipText("Validar usuario y acceder a la aplicación");
        btnCancelar.setToolTipText("Cancelar y cerrar la aplicación por completo");

        btnAceptar.addActionListener(e -> validar());
        btnCancelar.addActionListener(e -> System.exit(0));

        getRootPane().setDefaultButton(btnAceptar);

        // Panel sin JComboBox
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCampos.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCampos.add(new JLabel("Usuario:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtPassword);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        setLayout(new BorderLayout(10, 10));
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private void validar() {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        String rolSel = null;

        if (usuario.equalsIgnoreCase("admin") && pass.equals("1234")) {
            rolSel = "Admin";
        } else if (usuario.equalsIgnoreCase("user") && pass.equals("1234")) {
            rolSel = "Usuario";
        }

        if (rolSel != null) {
            dispose();
            MainApp app = new MainApp(rolSel, data);
            app.setVisible(true);
        } else {
            intentos++;
            JOptionPane.showMessageDialog(this,
                    "Credenciales incorrectas. Intenta de nuevo.",
                    "Error de acceso", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsuario.requestFocus();
            if (intentos >= 3) {
                JOptionPane.showMessageDialog(this,
                        "Demasiados intentos fallidos.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
