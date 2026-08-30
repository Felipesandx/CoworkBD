package edu.umg.programacion2.clase07.biblioteca.modelo;

/**
 * Representa un libro tal como se guarda en la tabla `libros`.
 *
 * Misma idea que Estudiante en la Clase 5: solo datos + encapsulamiento. No
 * sabe nada de SQL ni de prestamos - esa responsabilidad es de LibroDAO y
 * PrestamoDAO.
 */
public class Libro {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;

    public Libro(int id, String titulo, String autor, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }

    // Constructor de conveniencia para un libro que todavia no existe en la
    // base de datos (id = 0, MySQL le asigna el id real al insertarlo).
    public Libro(String titulo, String autor, String isbn) {
        this(0, titulo, autor, isbn);
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (ISBN %s)", id, titulo, autor, isbn);
    }
}
