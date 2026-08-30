package edu.umg.programacion2.clase07.biblioteca.servicio;

import edu.umg.programacion2.clase07.biblioteca.dao.LibroDAO;
import edu.umg.programacion2.clase07.biblioteca.dao.PrestamoDAO;
import edu.umg.programacion2.clase07.biblioteca.modelo.Libro;
import edu.umg.programacion2.clase07.biblioteca.modelo.PrestamoDetalle;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A diferencia de LibroDAO/PrestamoDAO (que solo hablan con MySQL), este
 * servicio combina datos que YA vinieron de la base de datos usando
 * colecciones de Java (List, Map, Set) para armar reportes. La idea clave:
 * la base de datos guarda los datos; las colecciones ayudan a resumirlos en
 * memoria una vez que ya los leiste.
 */
public class ReporteService {

    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;

    public ReporteService(LibroDAO libroDAO, PrestamoDAO prestamoDAO) {
        this.libroDAO = libroDAO;
        this.prestamoDAO = prestamoDAO;
    }

    /**
     * EJERCICIO DE LA CLASE: encontrar los libros que NUNCA se han prestado.
     *
     * Salida esperada con los datos de sql/schema.sql:
     *   [Cien anios de soledad, El principito, Introduction to Algorithms]
     *   (el orden puede variar, un Set no garantiza orden)
     *
     * Pistas:
     * 1. Consigue todos los libros con libroDAO.listarTodos().
     * 2. Consigue los prestamos activos con
     *    prestamoDAO.listarPrestamosActivosConLibro() (necesitas ese metodo
     *    ya resuelto). OJO: ese metodo te da el TITULO del libro, no el id -
     *    para este ejercicio con el titulo alcanza.
     * 3. Arma un Set<String> con los titulos que SI tienen prestamo activo
     *    (recorriendo el resultado del paso 2).
     * 4. Recorre todos los libros (paso 1) y agrega a un Set<Libro> los que
     *    su titulo NO este en el set del paso 3.
     *
     * Cuidado: este reporte solo considera prestamos ACTIVOS. Un libro que
     * ya fue devuelto (como "Clean Code" en los datos de ejemplo) SI cuenta
     * como "disponible ahora", pero tiene historial de prestamo - decide con
     * tu grupo si "nunca prestado" deberia significar "sin historial" en vez
     * de "sin prestamo activo", y ajusta la consulta si hace falta.
     */
    public Set<Libro> librosNuncaPrestados() throws SQLException {
        Set<Libro> resultado = new HashSet<>();
        // TODO: usar libroDAO y prestamoDAO para llenar "resultado" segun las pistas de arriba.

        return resultado;
    }

    /**
     * EJERCICIO DE LA CLASE: contar cuantas veces aparece cada titulo entre
     * los prestamos activos.
     *
     * Salida esperada con los datos de sql/schema.sql:
     *   {Clean Code=1, 1984=1}
     *
     * Pistas: es el mismo patron de "contador con HashMap" que ya conoces de
     * colecciones: por cada PrestamoDetalle, revisa si su titulo ya esta en
     * el mapa (si SI, suma 1; si NO, agregalo con valor 1).
     */
    public Map<String, Integer> contarPrestamosActivosPorTitulo() throws SQLException {
        Map<String, Integer> conteo = new HashMap<>();
        List<PrestamoDetalle> activos = prestamoDAO.listarPrestamosActivosConLibro();
        // TODO: recorrer "activos" y llenar "conteo" usando getTituloLibro() como llave.

        return conteo;
    }
}
