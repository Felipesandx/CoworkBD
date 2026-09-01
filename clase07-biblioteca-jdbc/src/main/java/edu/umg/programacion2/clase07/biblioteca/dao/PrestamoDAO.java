package edu.umg.programacion2.clase07.biblioteca.dao;

import edu.umg.programacion2.clase07.biblioteca.modelo.Prestamo;
import edu.umg.programacion2.clase07.biblioteca.modelo.PrestamoDetalle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de prestamos. Los primeros dos metodos (registrarPrestamo,
 * marcarDevuelto) son puro repaso de la Clase 5: un INSERT y un UPDATE con
 * PreparedStatement, exactamente como en EstudianteDAO.
 *
 * El tercer metodo, listarPrestamosActivosConLibro(), es el ejercicio nuevo
 * de esta clase: una consulta que combina DOS tablas con JOIN.
 */
public class PrestamoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/prog2_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "SomewhereIBelong";

    // Repaso: INSERT con generated keys, igual que EstudianteDAO.crear().
    public int registrarPrestamo(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamos (libro_id, nombre_estudiante, fecha_prestamo, fecha_devolucion) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, prestamo.getLibroId());
            statement.setString(2, prestamo.getNombreEstudiante());
            statement.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            statement.setNull(4, java.sql.Types.DATE);
            statement.executeUpdate();

            try (ResultSet claves = statement.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
                return -1;
            }
        }
    }

    // Repaso: UPDATE simple, igual que EstudianteDAO.actualizarNombre().
    public boolean marcarDevuelto(int prestamoId, LocalDate fechaDevolucion) throws SQLException {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE id = ? AND fecha_devolucion IS NULL";

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(fechaDevolucion));
            statement.setInt(2, prestamoId);

            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * EJERCICIO DE LA CLASE: listar todos los prestamos activos (fecha de
     * devolucion NULL) junto con el titulo del libro correspondiente.
     *
     * Sin JOIN tendrias que hacer un SELECT a prestamos y despues, POR CADA
     * fila, otro SELECT a libros para buscar el titulo (el clasico problema
     * "N+1 queries": una consulta extra por cada resultado). Con JOIN, la
     * base de datos arma la combinacion de las dos tablas en una sola
     * consulta.
     *
     * Salida esperada con los datos de sql/schema.sql:
     *   "Clean Code" prestado a Carlos Perez desde 2026-08-15
     *   "1984" prestado a Maria Gonzalez desde 2026-08-20
     *
     * Pistas:
     * 1. La consulta es:
     *      SELECT p.nombre_estudiante, p.fecha_prestamo, l.titulo
     *      FROM prestamos p
     *      JOIN libros l ON p.libro_id = l.id
     *      WHERE p.fecha_devolucion IS NULL
     *      ORDER BY p.fecha_prestamo
     * 2. p y l son "alias" de tabla: permiten escribir p.columna en vez de
     *    prestamos.columna, y evitan ambiguedad si dos tablas tuvieran una
     *    columna con el mismo nombre.
     * 3. Por cada fila del ResultSet, arma un PrestamoDetalle con
     *    resultado.getString("titulo"), resultado.getString("nombre_estudiante")
     *    y resultado.getDate("fecha_prestamo").toLocalDate().
     * 4. Agregalo a una List<PrestamoDetalle> y devuelvela al final.
     */
    public List<PrestamoDetalle> listarPrestamosActivosConLibro() throws SQLException {
       
        // TODO: ejecutar la consulta con JOIN descrita arriba y llenar "resultado".
        String sql = "SELECT  p.id  as IdPrestamo, p.nombre_estudiante as Estudiante,\n"
          + "  l.titulo as Titulo, l.autor as Autor, l.isbn, p.fecha_prestamo\n"
          + "FROM  prestamos p,\n"
          + "  libros l\n"
          + "WHERE  p.libro_id = l.id  ";
        List<PrestamoDetalle> prestamoDetalle = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet data = statement.executeQuery()) {

            while (data.next()) {
                prestamoDetalle.add(mapearFila(data));
                System.out.println(prestamoDetalle);
            }
        }
        

        return prestamoDetalle;
    }
    
    public int contarPrestamoPorLibro(int libroId) throws SQLException{
    	String sql = "SELECT COUNT(*) FROM prestamos WHERE libro_id = ?";
    	int total = 0;
    	
    	try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    		 PreparedStatement statement = conexion.prepareStatement(sql)){
    		
    		statement.setInt(1,libroId);
    			
    		try(ResultSet data = statement.executeQuery()){
    			if(data.next()) {
    				total = data.getInt(1);
    			}
    		}	
    	}
    	return total;
    }

    private PrestamoDetalle mapearFila(ResultSet resultado) throws SQLException {
        String tituloLibro = resultado.getString("Titulo");
        String nombreEstudiante = resultado.getString("Estudiante");
        String fechaPrestamo = resultado.getString("fecha_prestamo");
        return new PrestamoDetalle(tituloLibro, nombreEstudiante, fechaPrestamo);
    }
}
