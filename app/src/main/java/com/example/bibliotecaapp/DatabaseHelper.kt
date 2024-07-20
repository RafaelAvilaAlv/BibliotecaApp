package com.example.bibliotecaapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        ///agrege la creaicon de autores
        db.execSQL(SQL_CREATE_AUTORES)
        db.execSQL(SQL_CREATE_GENEROS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        //agrege la eliminacion de autores
        db.execSQL(SQL_DELETE_AUTORES)
        db.execSQL(SQL_DELETE_GENEROS)
        onCreate(db)
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

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS usuarios"
        ///igual esta implementado aqui
        private const val SQL_DELETE_AUTORES = "DROP TABLE IF EXISTS autor"
        private const val SQL_DELETE_GENEROS = "DROP TABLE IF EXISTS genero" // Añadir esta línea

    }
}