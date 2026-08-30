# Clase 7 - Biblioteca (JDBC con relacion 1:N) — Actividad de clase

Repaso directo de la Clase 5 (`clase05-jdbc-con-maven`): mismo patron de DAO,
`PreparedStatement`, `try-with-resources` y `Optional`. La novedad es que
ahora trabajamos con **dos tablas relacionadas** (`libros` y `prestamos`, un
libro puede tener muchos prestamos) y con un **JOIN** entre ellas.

## Estructura del proyecto

```
src/main/java/edu/umg/programacion2/clase07/biblioteca/
├── Main.java                        -> menu de consola
├── modelo/Libro.java                -> clase de dominio (tabla libros)
├── modelo/Prestamo.java             -> clase de dominio (tabla prestamos)
├── modelo/PrestamoDetalle.java      -> resultado del JOIN (no es una tabla)
├── dao/LibroDAO.java                -> CRUD de libros (repaso, ya resuelto)
├── dao/PrestamoDAO.java             -> CRUD de prestamos + JOIN (1 ejercicio)
└── servicio/ReporteService.java     -> reportes con colecciones (2 ejercicios)
```

## Preparacion

1. MySQL 8 corriendo en `localhost:3306` (el mismo que usaste en Clase 5).
2. Ejecuta `sql/schema.sql`:
   ```bash
   mysql -u root -p < sql/schema.sql
   ```
3. Ajusta `USUARIO` y `PASSWORD` en `LibroDAO.java` y `PrestamoDAO.java` con
   tus credenciales reales.
4. Compilar y correr:
   ```bash
   mvn compile
   mvn exec:java
   ```
   O en Eclipse: `File > Import... > Maven > Existing Maven Projects` y
   `Run As > Java Application` sobre `Main.java`.

## Que ya esta resuelto (repaso)

- `LibroDAO`: CRUD completo, identico en estilo a `EstudianteDAO` de la
  Clase 5.
- `PrestamoDAO.registrarPrestamo(...)` y `marcarDevuelto(...)`: un INSERT y
  un UPDATE, tambien puro repaso.

Si algo de esto no te queda claro, es buen momento para volver a
`clase05-jdbc-con-maven` y compararlo linea por linea.

## Ejercicios de la clase (3 TODOs)

1. **`PrestamoDAO.listarPrestamosActivosConLibro()`** — la consulta con
   `JOIN` entre `prestamos` y `libros`. Este es el ejercicio central: sin
   JOIN tendrias que hacer una consulta por cada prestamo para buscar el
   titulo de su libro (el problema "N+1 queries"). El metodo tiene la
   consulta SQL completa en el comentario, mas las pistas de como mapear el
   resultado.
2. **`ReporteService.librosNuncaPrestados()`** — combina `LibroDAO` y
   `PrestamoDAO` con un `Set` para encontrar los libros sin prestamo activo.
3. **`ReporteService.contarPrestamosActivosPorTitulo()`** — el patron de
   "contador con `HashMap`" que ya conoces, aplicado sobre el resultado del
   JOIN.

Cada uno tiene, en su javadoc: que debe hacer, la salida esperada con los
datos de `sql/schema.sql`, y pistas paso a paso.

**Criterio de evaluacion:** los 3 metodos compilan, no modifican las firmas
existentes ni `Main.java`, y la salida de las opciones 5, 6 y 7 del menu
coincide con el ejemplo documentado en cada metodo.

## Idea clave de la clase

- Un `JOIN` le pide a la base de datos que combine filas de dos (o mas)
  tablas relacionadas por una clave, en una sola consulta — es mucho mas
  eficiente que traer los datos por separado y combinarlos a mano en Java.
- Una vez que los datos ya estan en memoria (como una `List` que devolvio un
  DAO), volvemos a las herramientas de siempre — `Map`, `Set`, recorridos —
  para resumirlos en reportes.

## Ejercicio propuesto (para la casa)

Agrega un metodo `PrestamoDAO.contarPrestamosPorLibro(int libroId)` que
devuelva cuantas veces se ha prestado un libro especifico en toda su
historia (no solo los activos): `SELECT COUNT(*) FROM prestamos WHERE
libro_id = ?`. Es tu primer contacto con una funcion de agregacion SQL
(`COUNT`) — la vas a usar bastante en `clase07-cursos-inscripciones-jdbc`.
