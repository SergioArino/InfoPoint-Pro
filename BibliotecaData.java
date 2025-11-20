package projectInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class BibliotecaData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Libro> libros;
    private final ArrayList<Aviso> avisos;

    public BibliotecaData() {
        libros = new ArrayList<>();
        avisos = new ArrayList<>();
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    public ArrayList<Aviso> getAvisos() {
        return avisos;
    }

    public void addLibro(Libro l) {
        libros.add(l);
    }

    public void removeLibro(int index) {
        libros.remove(index);
    }

    public void addAviso(Aviso a) {
        avisos.add(a);
    }

    public void removeAviso(int index) {
        avisos.remove(index);
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static BibliotecaData loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (BibliotecaData) ois.readObject();
        }
    }

    public void copyFrom(BibliotecaData other) {
        this.libros.clear();
        this.libros.addAll(other.libros);
        this.avisos.clear();
        this.avisos.addAll(other.avisos);
    }
}
