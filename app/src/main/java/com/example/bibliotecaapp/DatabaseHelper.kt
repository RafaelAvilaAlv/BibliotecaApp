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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        //agrege la eliminacion de autores
        db.execSQL(SQL_DELETE_AUTORES)
        db.execSQL(SQL_DELETE_GENEROS)
        db.execSQL(SQL_DELETE_LIBROS)
        onCreate(db)
    }

    //Busqueda de libros
    fun searchBooks(name: String, category: String): List<Libros> {
        val db = readableDatabase
        val selection = StringBuilder()
        val selectionArgs = mutableListOf<String>()

        if (name.isNotEmpty()) {
            selection.append("titulo LIKE ?")
            selectionArgs.add("%$name%")
        }

        if (category.isNotEmpty()) {
            if (selection.isNotEmpty()) selection.append(" AND ")
            selection.append("genero_id IN (SELECT genero_id FROM genero WHERE nombre LIKE ?)")
            selectionArgs.add("%$category%")
        }

        val cursor = db.query(
            "libro",
            arrayOf("libro_id", "titulo", "autor_id", "genero_id"),
            if (selection.isNotEmpty()) selection.toString() else null,
            if (selectionArgs.isNotEmpty()) selectionArgs.toTypedArray() else null,
            null, null, null
        )

        val books = mutableListOf<Libros>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("libro_id"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val autorId = getInt(getColumnIndexOrThrow("autor_id"))
                val generoId = getInt(getColumnIndexOrThrow("genero_id"))
                books.add(Libros(id, titulo, autorId, generoId))
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

    fun insertLibro(libro: Libros) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", libro.titulo)
            put("autor_id", libro.autor_id)
            put("genero_id", libro.genero_id)
        }
        db.insert("libro", null, values)
    }


    companion object {
        const val DATABASE_VERSION = 1
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
                    "clave TEXT)"

        //aqui agrego las tablas de autores y paises
        private const val SQL_CREATE_AUTORES =
            "CREATE TABLE autor (" +
                    "autor_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombres TEXT," +
                    "apellidos TEXT," +
                    "fecha_nacimiento TEXT," +
                    "pais TEXT)"

        //aqui esta la tablla
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
                    "FOREIGN KEY(autor_id) REFERENCES autor(autor_id)," +
                    "FOREIGN KEY(genero_id) REFERENCES genero(genero_id))"


        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS usuarios"
        ///igual esta implementado aqui
        private const val SQL_DELETE_AUTORES = "DROP TABLE IF EXISTS autor"
        private const val SQL_DELETE_GENEROS = "DROP TABLE IF EXISTS genero" // Añadir esta línea
        private const val SQL_DELETE_LIBROS = "DROP TABLE IF EXISTS libro"


    }
}