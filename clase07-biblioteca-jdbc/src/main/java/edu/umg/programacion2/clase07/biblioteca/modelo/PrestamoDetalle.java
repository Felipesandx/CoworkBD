package edu.umg.programacion2.clase07.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Combina datos de `prestamos` y `libros` en un solo objeto: el titulo del
 * libro (que vive en la tabla libros) junto con quien lo tiene prestado (que
 * vive en la tabla prestamos).
 *
 * IMPORTANTE: esta clase NO corresponde a una tabla propia. Es el resultado
 * de un JOIN entre dos tablas, empaquetado para que Main no tenga que andar
 * combinando un Libro y un Prestamo por separado. Es normal (y muy comun) que
 * un DAO devuelva un objeto asi cuando la consulta junta varias tablas.
 */
public class PrestamoDetalle {

    private final String tituloLibro;
    private final String nombreEstudiante;
    private final String fechaPrestamo;

    public PrestamoDetalle(String tituloLibro, String nombreEstudiante, String fechaPrestamo) {
        this.tituloLibro = tituloLibro;
        this.nombreEstudiante = nombreEstudiante;
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" prestado a %s desde %s", tituloLibro, nombreEstudiante, fechaPrestamo);
    }
}
