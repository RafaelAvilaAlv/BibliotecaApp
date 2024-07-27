package com.example.bibliotecaapp


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        ///agrege la creaicon de autores
        db.execSQL(SQL_CREATE_AUTORES)
        db.execSQL(SQL_CREATE_GENEROS)
        db.execSQL(SQL_CREATE_LIBROS)
        db.execSQL(SQL_CREATE_PRESTAMOS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        //agrege la eliminacion de autores
        db.execSQL(SQL_DELETE_AUTORES)
        db.execSQL(SQL_DELETE_GENEROS)
        db.execSQL(SQL_DELETE_LIBROS)

        onCreate(db)
    }

    //Obtener id de libro
    fun getBookById(id: Int): Libros? {
        val db = this.readableDatabase
        var libro: Libros? = null
        val cursor = db.query(
            "libro", arrayOf("libro_id", "titulo", "autor_id", "genero_id", "image_path"),
            "libro_id=?", arrayOf(id.toString()), null, null, null, null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val libroId = cursor.getInt(cursor.getColumnIndexOrThrow("libro_id"))
                val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                val autorId = cursor.getInt(cursor.getColumnIndexOrThrow("autor_id"))
                val generoId = cursor.getInt(cursor.getColumnIndexOrThrow("genero_id"))
                val imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"))
                val autorNombre = getAutorNameById(autorId) // Método para obtener el nombre del autor
                val generoNombre = getGeneroNameById(generoId) // Método para obtener el nombre del género

                libro = Libros(libroId, titulo, autorId, generoId, autorNombre, generoNombre, imagePath)
            }
            cursor.close()
        }
        return libro
    }
    //  obtener el nombre del autor y del género por ID
    private fun getAutorNameById(autorId: Int): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombres FROM autor WHERE autor_id = ?", arrayOf(autorId.toString()))
        val autorNombre = if (cursor.moveToFirst()) cursor.getString(cursor.getColumnIndexOrThrow("nombres")) else ""
        cursor.close()
        return autorNombre
    }

    private fun getGeneroNameById(generoId: Int): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre FROM genero WHERE genero_id = ?", arrayOf(generoId.toString()))
        val generoNombre = if (cursor.moveToFirst()) cursor.getString(cursor.getColumnIndexOrThrow("nombre")) else ""
        cursor.close()
        return generoNombre
    }


    // Método para buscar libros
    fun searchBooks(name: String, category: String): List<Libros> {
        val db = readableDatabase
        val selection = StringBuilder()
        val selectionArgs = mutableListOf<String>()

        if (name.isNotEmpty()) {
            selection.append("libro.titulo LIKE ?")
            selectionArgs.add("%$name%")
        }

        if (category.isNotEmpty()) {
            if (selection.isNotEmpty()) selection.append(" AND ")
            selection.append("libro.genero_id IN (SELECT genero.genero_id FROM genero WHERE genero.nombre LIKE ?)")
            selectionArgs.add("%$category%")
        }

        val query = """
            SELECT libro.libro_id, libro.titulo, libro.autor_id, libro.genero_id, libro.image_path,
                   autor.nombres AS autor_nombre, genero.nombre AS genero_nombre
            FROM libro
            INNER JOIN autor ON libro.autor_id = autor.autor_id
            INNER JOIN genero ON libro.genero_id = genero.genero_id
            WHERE ${if (selection.isNotEmpty()) selection.toString() else "1=1"}
        """

        val cursor = db.rawQuery(query, selectionArgs.toTypedArray())
        val books = mutableListOf<Libros>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("libro_id"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val autorId = getInt(getColumnIndexOrThrow("autor_id"))
                val generoId = getInt(getColumnIndexOrThrow("genero_id"))
                val imagePath = getString(getColumnIndexOrThrow("image_path"))
                val autorNombre = getString(getColumnIndexOrThrow("autor_nombre"))
                val generoNombre = getString(getColumnIndexOrThrow("genero_nombre"))

                books.add(Libros(id, titulo, autorId, generoId, autorNombre, generoNombre, imagePath))
            }
        }
        cursor.close()
        return books
    }

    // Método para obtener autores
    fun getAllAutores(): List<String> {
        val autores = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombres FROM autor", null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombres"))
                autores.add(nombre)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return autores
    }

    // Método para obtener géneros
    fun getAllGeneros(): List<String> {
        val generos = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre FROM genero", null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                generos.add(nombre)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return generos
    }

    // Método para insertar libro
    fun insertLibro(libro: Libros) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", libro.titulo)
            put("autor_id", libro.autor_id)
            put("genero_id", libro.genero_id)
            put("image_path", libro.imagePath) // Agregar esta línea
        }
        db.insert("libro", null, values)
    }

    //Historial de prestamos por id de usuario
    fun getPrestamosByUserId(userId: Int): List<Prestamo> {
        val prestamos = mutableListOf<Prestamo>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM prestamos WHERE usuario_id = ?", arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"))
                val libroId = cursor.getInt(cursor.getColumnIndexOrThrow("libro_id"))
                val fechaPrestamo = cursor.getString(cursor.getColumnIndexOrThrow("fecha_prestamo"))
                val fechaDevolucion = cursor.getString(cursor.getColumnIndexOrThrow("fecha_devolucion"))
                val prestamo = Prestamo(id, usuarioId, libroId, fechaPrestamo, fechaDevolucion)
                prestamos.add(prestamo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return prestamos
    }

    //Metodo para guardar pedidos
    fun insertPedido(prestamo: Prestamo) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("usuario_id", prestamo.usuario_id)
            put("libro_id", prestamo.libro_id)
            put("fecha_prestamo", prestamo.fecha_prestamo)
            put("fecha_devolucion", prestamo.fecha_devolucion)
        }
        db.insert("pedidos", null, values)
    }


    // Método para obtener el ID del autor basado en el nombre
    fun getAutorIdByName(nombre: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT autor_id FROM autor WHERE nombres = ?", arrayOf(nombre))
        val autorId = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow("autor_id")) else null
        cursor.close()
        return autorId
    }

    // Método para obtener el ID del género basado en el nombre
    fun getGeneroIdByName(nombre: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT genero_id FROM genero WHERE nombre = ?", arrayOf(nombre))
        val generoId = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow("genero_id")) else null
        cursor.close()
        return generoId
    }
//Metodo para obtener el titulo del libro basado en el id
    fun getLibroTitleById(libroId: Int): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT titulo FROM libros WHERE id = ?", arrayOf(libroId.toString()))

        var titulo = "Título no encontrado"
        if (cursor.moveToFirst()) {
            titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
        }
        cursor.close()
        return titulo
    }


    // Método para recuperar los libros
    fun getAllBooks(): List<Libros> {
        val db = readableDatabase
        val query = """
            SELECT libro.libro_id, libro.titulo, libro.autor_id, libro.genero_id, libro.image_path,
                   autor.nombres AS autor_nombre, genero.nombre AS genero_nombre
            FROM libro
            INNER JOIN autor ON libro.autor_id = autor.autor_id
            INNER JOIN genero ON libro.genero_id = genero.genero_id
        """
        val cursor = db.rawQuery(query, null)
        val books = mutableListOf<Libros>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("libro_id"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val autorId = getInt(getColumnIndexOrThrow("autor_id"))
                val generoId = getInt(getColumnIndexOrThrow("genero_id"))
                val imagePath = getString(getColumnIndexOrThrow("image_path"))
                val autorNombre = getString(getColumnIndexOrThrow("autor_nombre"))
                val generoNombre = getString(getColumnIndexOrThrow("genero_nombre"))

                books.add(Libros(id, titulo, autorId, generoId, autorNombre, generoNombre, imagePath))
            }
        }
        cursor.close()
        return books
    }

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "BibliotecaApp.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY," +
                    "nombre TEXT," +
                    "apellido TEXT," +
                    "cedula TEXT," +
                    "correo TEXT," +
                    "direccion TEXT," +
                    "telefono TEXT," +
                    "usuario TEXT," +
                    "clave TEXT,"+
                    "foto TEXT)"


        private const val SQL_CREATE_AUTORES =
            "CREATE TABLE autor (" +
                    "autor_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombres TEXT," +
                    "apellidos TEXT," +
                    "fecha_nacimiento TEXT," +
                    "pais TEXT)"

        private const val SQL_CREATE_GENEROS =
            "CREATE TABLE genero (" +
                    "genero_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT)"

        private const val SQL_CREATE_LIBROS =
            "CREATE TABLE libro (" +
                    "libro_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT," +
                    "autor_id INTEGER," +
                    "genero_id INTEGER," +
                    "image_path TEXT," + // Añadido para la ruta de la imagen
                    "FOREIGN KEY(autor_id) REFERENCES autor(autor_id)," +
                    "FOREIGN KEY(genero_id) REFERENCES genero(genero_id))"


        private const val SQL_CREATE_PRESTAMOS =
            "CREATE TABLE prestamo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER," +
                    "libro_id INTEGER," +
                    "fecha_prestamo TEXT," +
                    "fecha_devolucion TEXT," +
                    "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)," +
                    "FOREIGN KEY(libro_id) REFERENCES libro(libro_id))"


        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS usuarios"
        private const val SQL_DELETE_AUTORES = "DROP TABLE IF EXISTS autor"
        private const val SQL_DELETE_GENEROS = "DROP TABLE IF EXISTS genero"
        private const val SQL_DELETE_LIBROS = "DROP TABLE IF EXISTS libro"
    }
}
