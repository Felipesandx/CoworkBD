package edu.umg.programacion2.clase07.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Representa un prestamo tal como se guarda en la tabla `prestamos`.
 *
 * IMPORTANTE: fechaDevolucion es null mientras el libro sigue prestado. Un
 * prestamo "activo" es exactamente eso: uno cuya fechaDevolucion todavia no
 * se ha registrado. El metodo esActivo() es el que van a usar en toda la
 * clase para distinguir "prestado ahora mismo" de "historial ya cerrado".
 */
public class Prestamo {

    private int id;
    private int libroId;
    private String nombreEstudiante;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public Prestamo(int id, int libroId, String nombreEstudiante, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.id = id;
        this.libroId = libroId;
        this.nombreEstudiante = nombreEstudiante;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Constructor de conveniencia para registrar un prestamo nuevo: todavia
    // no tiene id (lo asigna MySQL) ni fecha de devolucion (arranca activo).
    public Prestamo(int libroId, String nombreEstudiante, LocalDate fechaPrestamo) {
        this(0, libroId, nombreEstudiante, fechaPrestamo, null);
    }

    public int getId() {
        return id;
    }

    public int getLibroId() {
        return libroId;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    // Cuidado: esto es logica de negocio simple (no SQL), por eso vive en el
    // modelo y no en el DAO: "activo" significa "sin fecha de devolucion".
    public boolean esActivo() {
        return fechaDevolucion == null;
    }

    @Override
    public String toString() {
        String estado = esActivo() ? "ACTIVO" : "devuelto " + fechaDevolucion;
        return String.format("[%d] libro #%d - %s (prestado %s, %s)",
                id, libroId, nombreEstudiante, fechaPrestamo, estado);
    }
}
