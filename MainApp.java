package projectInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

public class MainApp extends JFrame {

    private final BibliotecaData data;
    private final String rol;
    private PanelAdmin panelAdmin;
    private PanelUsuario panelUsuario;

    public MainApp(String rol, BibliotecaData data) {
        super("Biblioteca Municipal - Rol: " + rol);
        this.rol = rol;
        this.data = data;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        crearBarraMenu();

        if ("Admin".equalsIgnoreCase(rol)) {
            panelAdmin = new PanelAdmin(data);
            add(panelAdmin, BorderLayout.CENTER);
            getRootPane().setDefaultButton(panelAdmin.getDefaultButton());
        } else {
            panelUsuario = new PanelUsuario(data);
            add(panelUsuario, BorderLayout.CENTER);
        }

        pack();
        setLocationRelativeTo(null);
    }

    private void crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();

        // MENÚ ARCHIVO
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic(KeyEvent.VK_A);

        JMenuItem miAbrir = new JMenuItem("Abrir datos...");
        miAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        miAbrir.addActionListener(e -> cargarDatos());

        JMenuItem miGuardar = new JMenuItem("Guardar datos...");
        miGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        miGuardar.addActionListener(e -> guardarDatos());

        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        miSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(miAbrir);
        menuArchivo.add(miGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(miSalir);

        JMenu menuVer = new JMenu("Ver");

        JMenuItem miNimbus = new JMenuItem("Nimbus");
        miNimbus.addActionListener(e -> cambiarLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"));

        JMenuItem miMetal = new JMenuItem("Metal");
        miMetal.addActionListener(e -> cambiarLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"));

        JMenuItem miWindows = new JMenuItem("Windows");
        miWindows.addActionListener(e -> cambiarLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));

        menuVer.add(miNimbus);
        menuVer.add(miMetal);
        menuVer.add(miWindows);

        // MENÚ EDICIÓN
        JMenu menuEdicion = new JMenu("Edición");

        JMenuItem miDeshacer = new JMenuItem("Deshacer");
        miDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        miDeshacer.addActionListener(e -> {
            if (panelUsuario != null) panelUsuario.deshacer();
        });

        JMenuItem miRehacer = new JMenuItem("Rehacer");
        miRehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        miRehacer.addActionListener(e -> {
            if (panelUsuario != null) panelUsuario.rehacer();
        });

        menuEdicion.add(miDeshacer);
        menuEdicion.add(miRehacer);

        // MENÚ AYUDA
        JMenu menuAyuda = new JMenu("Ayuda");

        JMenuItem miAcercaDe = new JMenuItem("Acerca de...");
        miAcercaDe.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Biblioteca Municipal\nDemo de usabilidad, accesibilidad y robustez en Swing.",
                    "Acerca de", JOptionPane.INFORMATION_MESSAGE);
        });

        menuAyuda.add(miAcercaDe);

        // AGREGAR TODO A LA BARRA
        menuBar.add(menuArchivo);
        menuBar.add(menuVer);
        menuBar.add(menuEdicion);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }

    private void cambiarLookAndFeel(String laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
            pack();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo aplicar el tema seleccionado.",
                    "Error Look & Feel", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarDatos() {
        try {
            data.saveToFile("biblioteca.dat");
            JOptionPane.showMessageDialog(this,
                    "Datos guardados correctamente en biblioteca.dat",
                    "Guardar datos", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        File f = new File("biblioteca.dat");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this,
                    "No existe el archivo biblioteca.dat",
                    "Cargar datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BibliotecaData cargada = BibliotecaData.loadFromFile("biblioteca.dat");
            data.copyFrom(cargada);

            if (panelAdmin != null) panelAdmin.recargarDatos();
            if (panelUsuario != null) panelUsuario.recargarResultados();

            JOptionPane.showMessageDialog(this,
                    "Datos cargados correctamente.",
                    "Cargar datos", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BibliotecaData data = new BibliotecaData();
            File f = new File("biblioteca.dat");
            if (f.exists()) {
                try {
                    BibliotecaData cargada = BibliotecaData.loadFromFile("biblioteca.dat");
                    data.copyFrom(cargada);
                } catch (Exception ex) {
                    System.err.println("No se pudieron cargar datos previos: " + ex.getMessage());
                }
            }
            SplashScreen splash = new SplashScreen(null, data);
            splash.iniciarCarga();
        });
    }
}
