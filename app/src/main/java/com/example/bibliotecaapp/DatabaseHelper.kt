package com.example.bibliotecaapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, apellido TEXT, usuario TEXT, clave TEXT)"
        db.execSQL(createTable)

        // Insertar usuario administrador
        db.execSQL("INSERT INTO usuarios (nombre, apellido, usuario, clave) VALUES ('Admin', 'Admin', 'admin', '1234')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "bibliotec.db"
        private const val DATABASE_VERSION = 1
    }
}