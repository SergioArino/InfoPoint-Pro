package projectInfo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JComponent;

import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

public class PanelUsuario extends JPanel {

    private final BibliotecaData data;

    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTextArea txtResultados;
    private JTextArea txtComentarios;

    private final UndoManager undoManager;

    public PanelUsuario(BibliotecaData data) {
        this.data = data;
        this.undoManager = new UndoManager();

        setLayout(new BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(new TitledBorder("Búsqueda de libros"));

        JLabel lblBuscar = new JLabel("Buscar (título o autor): ");
        txtBusqueda = new JTextField();
        btnBuscar = new JButton("Buscar");
        btnBuscar.setMnemonic(KeyEvent.VK_B);
        btnBuscar.setToolTipText("Buscar libros por título o autor");

        btnBuscar.addActionListener(e -> realizarBusqueda());
        txtBusqueda.addActionListener(e -> realizarBusqueda());

        JPanel panelCampos = new JPanel(new BorderLayout(5, 5));
        panelCampos.add(lblBuscar, BorderLayout.WEST);
        panelCampos.add(txtBusqueda, BorderLayout.CENTER);

        panelBusqueda.add(panelCampos, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        txtResultados = new JTextArea(8, 40);
        txtResultados.setEditable(false);
        txtResultados.setLineWrap(true);
        txtResultados.setWrapStyleWord(true);
        txtResultados.setToolTipText("Resultados de la búsqueda de libros");
        JScrollPane scrollResultados = new JScrollPane(txtResultados);
        scrollResultados.setBorder(new TitledBorder("Resultados"));

        txtComentarios = new JTextArea(5, 40);
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);
        txtComentarios.setToolTipText("Área de comentarios del usuario. Admite deshacer y rehacer.");

        txtComentarios.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        InputMap im = txtComentarios.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = txtComentarios.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Deshacer");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "Rehacer");

        am.put("Deshacer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deshacer();
            }
        });

        am.put("Rehacer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rehacer();
            }
        });

        JScrollPane scrollComentarios = new JScrollPane(txtComentarios);
        scrollComentarios.setBorder(new TitledBorder("Comentarios del usuario"));

        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollResultados, BorderLayout.CENTER);
        add(scrollComentarios, BorderLayout.SOUTH);
    }

    private void realizarBusqueda() {
        String texto = txtBusqueda.getText().trim().toLowerCase();
        StringBuilder sb = new StringBuilder();

        if (texto.isEmpty()) {
            sb.append("Introduce un texto de búsqueda.\n");
        } else {
            for (Libro l : data.getLibros()) {
                if (l.getTitulo().toLowerCase().contains(texto) ||
                        l.getAutor().toLowerCase().contains(texto)) {
                    sb.append("- ").append(l.getTitulo())
                            .append(" (").append(l.getAutor())
                            .append(", ").append(l.getAnio())
                            .append(", ").append(l.getPaginas()).append(" págs.)\n");
                }
            }
            if (sb.length() == 0) {
                sb.append("No se encontraron libros que coincidan con la búsqueda.\n");
            }
        }
        txtResultados.setText(sb.toString());
    }

    public void recargarResultados() {
        realizarBusqueda();
    }

    public void deshacer() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void rehacer() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
}
