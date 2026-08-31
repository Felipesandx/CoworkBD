package edu.umg.programacion2.clase07.biblioteca;

import edu.umg.programacion2.clase07.biblioteca.dao.LibroDAO;
import edu.umg.programacion2.clase07.biblioteca.dao.PrestamoDAO;
import edu.umg.programacion2.clase07.biblioteca.modelo.Libro;
import edu.umg.programacion2.clase07.biblioteca.modelo.Prestamo;
import edu.umg.programacion2.clase07.biblioteca.modelo.PrestamoDetalle;
import edu.umg.programacion2.clase07.biblioteca.servicio.ReporteService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * Clase 7 - Biblioteca: repaso de JDBC/DAO (Clase 5) sobre dos tablas
 * relacionadas (libros y prestamos), mas un JOIN y reportes con colecciones.
 *
 * Igual que en Clase 5, Main SOLO muestra el menu y lee lo que escribe el
 * usuario. Toda la logica de base de datos vive en LibroDAO/PrestamoDAO, y
 * los reportes en memoria viven en ReporteService.
 */
public class Main {

    private static final Scanner teclado = new Scanner(System.in);
    private static final LibroDAO libroDAO = new LibroDAO();
    private static final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private static final ReporteService reporteService = new ReporteService(libroDAO, prestamoDAO);

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    listarLibros();
                    break;
                case 2:
                    buscarLibroPorIsbn();
                    break;
                case 3:
                    registrarPrestamo();
                    break;
                case 4:
                    marcarDevuelto();
                    break;
                case 5:
                    listarPrestamosActivos();
                    break;
                case 6:
                    reporteLibrosNuncaPrestados();
                    break;
                case 7:
                    reporteConteoPorTitulo();
                    break;
                case 8:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intenta de nuevo.");
            }
            System.out.println();
        } while (opcion != 8);

        teclado.close();
    }

    private static void mostrarMenu() {
        System.out.println("=== Biblioteca (MySQL) ===");
        System.out.println("1. Listar libros");
        System.out.println("2. Buscar libro por ISBN");
        System.out.println("3. Registrar prestamo");
        System.out.println("4. Marcar prestamo como devuelto");
        System.out.println("5. Ver prestamos activos (con titulo del libro)");
        System.out.println("6. Reporte: libros nunca prestados");
        System.out.println("7. Reporte: conteo de prestamos activos por titulo");
        System.out.println("8. Salir");
        System.out.print("Elige una opcion: ");
    }

    // Cuidado: mismo truco de la Clase 5 para que Scanner.nextInt() no deje
    // pendiente el "Enter" cuando despues se usa nextLine().
    private static int leerOpcion() {
        while (!teclado.hasNextInt()) {
            System.out.print("Escribe un numero valido: ");
            teclado.next();
        }
        int opcion = teclado.nextInt();
        teclado.nextLine();
        return opcion;
    }

    private static void listarLibros() {
        try {
            List<Libro> libros = libroDAO.listarTodos();
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados todavia.");
                return;
            }
            for (Libro libro : libros) {
                System.out.println(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los libros: " + e.getMessage());
        }
    }

    private static void buscarLibroPorIsbn() {
        System.out.print("ISBN a buscar: ");
        String isbn = teclado.nextLine();

        try {
            Optional<Libro> libro = libroDAO.buscarPorIsbn(isbn);
            if (libro.isPresent()) {
                System.out.println("Encontrado: " + libro.get());
            } else {
                System.out.println("No existe ningun libro con ese ISBN.");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private static void registrarPrestamo() {
        System.out.print("ID del libro: ");
        int libroId = leerEntero();
        System.out.print("Nombre del estudiante: ");
        String nombreEstudiante = teclado.nextLine();

        try {
            Prestamo prestamo = new Prestamo(libroId, nombreEstudiante, LocalDate.now());
            int id = prestamoDAO.registrarPrestamo(prestamo);
            System.out.println("Prestamo registrado con id " + id);
        } catch (SQLException e) {
            System.err.println("Error al registrar el prestamo: " + e.getMessage());
        }
    }

    private static void marcarDevuelto() {
        System.out.print("ID del prestamo a marcar como devuelto: ");
        int prestamoId = leerEntero();

        try {
            boolean actualizado = prestamoDAO.marcarDevuelto(prestamoId, LocalDate.now());
            if (actualizado) {
                System.out.println("Prestamo marcado como devuelto.");
            } else {
                System.out.println("No existe un prestamo activo con ese id.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el prestamo: " + e.getMessage());
        }
    }

    private static void listarPrestamosActivos() {
        try {
            List<PrestamoDetalle> activos = prestamoDAO.listarPrestamosActivosConLibro();
            if (activos.isEmpty()) {
                System.out.println("No hay prestamos activos (o el metodo con JOIN todavia no esta completo).");
                return;
            }
            for (PrestamoDetalle detalle : activos) {
                System.out.println(detalle);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los prestamos activos: " + e.getMessage());
        }
    }

    private static void reporteLibrosNuncaPrestados() {
        try {
            Set<Libro> libros = reporteService.librosNuncaPrestados();
            System.out.println(libros);
        } catch (SQLException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    private static void reporteConteoPorTitulo() {
        try {
            Map<String, Integer> conteo = reporteService.contarPrestamosActivosPorTitulo();
            System.out.println(conteo);
        } catch (SQLException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    private static int leerEntero() {
        while (!teclado.hasNextInt()) {
            System.out.print("Escribe un numero valido: ");
            teclado.next();
        }
        int valor = teclado.nextInt();
        teclado.nextLine();
        return valor;
    }
}
