-- Clase 7 - Biblioteca: dos tablas relacionadas (1:N)
-- Ejecuta este script en MySQL Workbench o desde la consola ANTES de correr el proyecto Java.
--
-- Usa la misma base que ya tienes de la Clase 5 (prog2_db). IF NOT EXISTS evita
-- error si la base ya existe.

CREATE DATABASE IF NOT EXISTS prog2_db;

USE prog2_db;

CREATE TABLE IF NOT EXISTS libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE
);

-- Cuidado: fecha_devolucion puede ser NULL. Un prestamo con fecha_devolucion
-- NULL significa "todavia esta prestado" (activo). Cuando el libro se
-- devuelve, se actualiza esa columna con la fecha real.
CREATE TABLE IF NOT EXISTS prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libro_id INT NOT NULL,
    nombre_estudiante VARCHAR(100) NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NULL,
    FOREIGN KEY (libro_id) REFERENCES libros(id)
);

INSERT IGNORE INTO libros (id, titulo, autor, isbn) VALUES
    (1, 'Cien anios de soledad', 'Gabriel Garcia Marquez', '978-0307474728'),
    (2, 'El principito', 'Antoine de Saint-Exupery', '978-0156012195'),
    (3, 'Clean Code', 'Robert C. Martin', '978-0132350884'),
    (4, 'Introduction to Algorithms', 'Cormen, Leiserson, Rivest, Stein', '978-0262033848'),
    (5, '1984', 'George Orwell', '978-0451524935');

-- Prestamos de ejemplo: el libro 3 tiene dos prestamos (uno ya devuelto, uno
-- activo); el libro 5 tiene un prestamo activo; los libros 1, 2 y 4 nunca se
-- han prestado (utiles para el reporte de "libros nunca prestados").
INSERT IGNORE INTO prestamos (id, libro_id, nombre_estudiante, fecha_prestamo, fecha_devolucion) VALUES
    (1, 3, 'Ana Lopez', '2026-08-01', '2026-08-10'),
    (2, 3, 'Carlos Perez', '2026-08-15', NULL),
    (3, 5, 'Maria Gonzalez', '2026-08-20', NULL);
