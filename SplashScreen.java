package projectInfo;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Frame;

public class SplashScreen extends JDialog {

    private final JProgressBar progressBar;
    private final JLabel lblEstado;
    private final BibliotecaData data;

    public SplashScreen(Frame owner, BibliotecaData data) {
        super(owner, true);
        this.data = data;
        setUndecorated(true);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblEstado = new JLabel("Iniciando...", SwingConstants.CENTER);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(lblEstado, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public void iniciarCarga() {
        new Thread(() -> {
            String[] mensajes = {
                    "Conectando...",
                    "Cargando estilos...",
                    "Preparando datos...",
                    "Inicializando interfaz..."
            };
            for (int i = 0; i <= 100; i += 10) {
                int idx = Math.min(i / 25, mensajes.length - 1);
                final int valor = i;
                final String msg = mensajes[idx];

                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(valor);
                    lblEstado.setText(msg);
                });

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }
            }

            SwingUtilities.invokeLater(() -> {
                dispose();
                LoginDialog login = new LoginDialog(null, data);
                login.setVisible(true);
            });
        }).start();

        setVisible(true);
    }
}
