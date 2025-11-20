package projectInfo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelAdmin extends JPanel {

    private final BibliotecaData data;

    // Panel contenedor central donde cambia la vista
    private JPanel panelCentral;

    // Paneles de cada función
    private JPanel panelLibros;
    private JPanel panelAvisos;

    // ----- LIBROS -----
    private JTextField txtTituloLibro;
    private JTextField txtAutorLibro;
    private JTextField txtAnioLibro;
    private JTextField txtPaginasLibro;
    private JTable tablaLibros;
    private DefaultTableModel modeloLibros;
    private JButton btnAgregarLibro;

    // ----- AVISOS -----
    private JTextField txtTituloAviso;
    private JTextArea txtDescripcionAviso;
    private JTable tablaAvisos;
    private DefaultTableModel modeloAvisos;

    public PanelAdmin(BibliotecaData data) {
        this.data = data;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -------------------------------
        // BOTONES SUPERIORES PARA CAMBIAR
        // -------------------------------
        JPanel panelBotonesCambio = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnLibros = new JButton("Gestión de Libros");
        JButton btnAvisos = new JButton("Gestión de Avisos");

        panelBotonesCambio.add(btnLibros);
        panelBotonesCambio.add(btnAvisos);

        // Panel central donde se irán mostrando los paneles
        panelCentral = new JPanel(new BorderLayout());

        // Crear ambos paneles
        panelLibros = crearPanelLibros();
        panelAvisos = crearPanelAvisos();

        // Mostrar por defecto Libros
        panelCentral.add(panelLibros, BorderLayout.CENTER);

        // Eventos cambio de vista
        btnLibros.addActionListener(e -> cambiarVista(panelLibros));
        btnAvisos.addActionListener(e -> cambiarVista(panelAvisos));

        add(panelBotonesCambio, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    // Cambia la vista en el panel central
    private void cambiarVista(JPanel nuevaVista) {
        panelCentral.removeAll();
        panelCentral.add(nuevaVista, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    // ------------------------------------------------------
    // ---------------------- LIBROS ------------------------
    // ------------------------------------------------------

    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.setBorder(new TitledBorder("Datos del libro"));

        txtTituloLibro = new JTextField();
        txtAutorLibro = new JTextField();
        txtAnioLibro = new JTextField();
        txtPaginasLibro = new JTextField();

        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTituloLibro);
        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtAutorLibro);
        panelFormulario.add(new JLabel("Año:"));
        panelFormulario.add(txtAnioLibro);
        panelFormulario.add(new JLabel("Páginas:"));
        panelFormulario.add(txtPaginasLibro);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.setBorder(new TitledBorder("Acciones"));

        btnAgregarLibro = new JButton("Añadir");
        btnAgregarLibro.setMnemonic(KeyEvent.VK_A);

        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelAcciones.add(btnAgregarLibro);
        panelAcciones.add(btnModificar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnLimpiar);

        modeloLibros = new DefaultTableModel(new Object[]{"Título", "Autor", "Año", "Páginas"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaLibros = new JTable(modeloLibros);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaLibros);
        scroll.setBorder(new TitledBorder("Listado de libros"));

        // Eventos
        btnAgregarLibro.addActionListener(e -> agregarLibro());
        btnModificar.addActionListener(e -> modificarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormularioLibros());

        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarLibroSeleccionado();
        });

        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(panelAcciones, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        recargarTablaLibros();
        return panel;
    }

    // ------------------------------------------------------
    // ---------------------- AVISOS -------------------------
    // ------------------------------------------------------

    private JPanel crearPanelAvisos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new BorderLayout(5, 5));
        panelFormulario.setBorder(new TitledBorder("Nuevo aviso"));

        JPanel panelCampos = new JPanel(new GridLayout(1, 2, 5, 5));
        txtTituloAviso = new JTextField();

        panelCampos.add(new JLabel("Título:"));
        panelCampos.add(txtTituloAviso);

        txtDescripcionAviso = new JTextArea(3, 20);
        txtDescripcionAviso.setLineWrap(true);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcionAviso);

        panelFormulario.add(panelCampos, BorderLayout.NORTH);
        panelFormulario.add(scrollDesc, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.setBorder(new TitledBorder("Acciones"));

        JButton btnAgregar = new JButton("Añadir aviso");
        JButton btnEliminar = new JButton("Eliminar aviso");

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEliminar);

        modeloAvisos = new DefaultTableModel(new Object[]{"Título", "Descripción"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaAvisos = new JTable(modeloAvisos);
        tablaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollAvisos = new JScrollPane(tablaAvisos);
        scrollAvisos.setBorder(new TitledBorder("Listado de avisos"));

        btnAgregar.addActionListener(e -> agregarAviso());
        btnEliminar.addActionListener(e -> eliminarAviso());

        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(panelAcciones, BorderLayout.CENTER);
        panel.add(scrollAvisos, BorderLayout.SOUTH);

        recargarTablaAvisos();

        return panel;
    }

    // ------------------------------------------------------
    // ----------- MÉTODOS FUNCIONALES ----------------------
    // ------------------------------------------------------

    public JButton getDefaultButton() {
        return btnAgregarLibro;
    }

    public void recargarDatos() {
        recargarTablaLibros();
        recargarTablaAvisos();
    }

    private void recargarTablaLibros() {
        modeloLibros.setRowCount(0);
        for (Libro l : data.getLibros()) {
            modeloLibros.addRow(new Object[]{l.getTitulo(), l.getAutor(), l.getAnio(), l.getPaginas()});
        }
    }

    private void recargarTablaAvisos() {
        modeloAvisos.setRowCount(0);
        for (Aviso a : data.getAvisos()) {
            modeloAvisos.addRow(new Object[]{a.getTitulo(), a.getDescripcion()});
        }
    }

    private Integer leerEntero(JTextField txt, String nombre) {
        try { return Integer.parseInt(txt.getText().trim()); }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El campo " + nombre + " debe ser numérico.");
            txt.requestFocus();
            return null;
        }
    }

    private void agregarLibro() {
        String titulo = txtTituloLibro.getText().trim();
        String autor = txtAutorLibro.getText().trim();
        if (titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título y autor obligatorios."); return;
        }
        Integer anio = leerEntero(txtAnioLibro, "Año"); if (anio == null) return;
        Integer pags = leerEntero(txtPaginasLibro, "Páginas"); if (pags == null) return;

        data.addLibro(new Libro(titulo, autor, anio, pags));
        recargarTablaLibros();
        limpiarFormularioLibros();
    }

    private void modificarLibro() {
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un libro."); return; }

        Libro l = data.getLibros().get(fila);

        Integer anio = leerEntero(txtAnioLibro, "Año"); if (anio == null) return;
        Integer pags = leerEntero(txtPaginasLibro, "Páginas"); if (pags == null) return;

        l.setTitulo(txtTituloLibro.getText().trim());
        l.setAutor(txtAutorLibro.getText().trim());
        l.setAnio(anio);
        l.setPaginas(pags);

        recargarTablaLibros();
    }

    private void eliminarLibro() {
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un libro."); return; }

        if (JOptionPane.showConfirmDialog(this, "¿Eliminar libro?", "Confirmar", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
            data.removeLibro(fila);
            recargarTablaLibros();
            limpiarFormularioLibros();
        }
    }

    private void limpiarFormularioLibros() {
        txtTituloLibro.setText("");
        txtAutorLibro.setText("");
        txtAnioLibro.setText("");
        txtPaginasLibro.setText("");
        tablaLibros.clearSelection();
    }

    private void cargarLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila >= 0) {
            Libro l = data.getLibros().get(fila);
            txtTituloLibro.setText(l.getTitulo());
            txtAutorLibro.setText(l.getAutor());
            txtAnioLibro.setText(String.valueOf(l.getAnio()));
            txtPaginasLibro.setText(String.valueOf(l.getPaginas()));
        }
    }

    private void agregarAviso() {
        String titulo = txtTituloAviso.getText().trim();
        String desc = txtDescripcionAviso.getText().trim();
        if (titulo.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título y descripción obligatorios."); return;
        }
        data.addAviso(new Aviso(titulo, desc));
        recargarTablaAvisos();
        txtTituloAviso.setText("");
        txtDescripcionAviso.setText("");
    }

    private void eliminarAviso() {
        int fila = tablaAvisos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un aviso."); return; }

        if (JOptionPane.showConfirmDialog(this, "¿Eliminar aviso?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            data.removeAviso(fila);
            recargarTablaAvisos();
        }
    }
}
