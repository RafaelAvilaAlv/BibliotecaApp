package com.example.bibliotecaapp


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AgregarLibros : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var spinnerAutor: Spinner
    private lateinit var spinnerGenero: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_libros)

        databaseHelper = DatabaseHelper(this)

        spinnerAutor = findViewById(R.id.spinnerAutor)
        spinnerGenero = findViewById(R.id.spinnerGenero)

        populateSpinners()
    }

    private fun populateSpinners() {
        val autores = mutableListOf("Escoge un autor")
        autores.addAll(databaseHelper.getAllAutores())

        val generos = mutableListOf("Escoge un género")
        generos.addAll(databaseHelper.getAllGeneros())

        val autorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, autores)
        autorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAutor.adapter = autorAdapter

        val generoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = generoAdapter
    }



}
