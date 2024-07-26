package com.example.bibliotecaapp


//ESTOOOO AHI QUE AGREGAR MAÑANA EL GUARDAR LIBRO ACUERDATE PORFA

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BuscarLib : AppCompatActivity() {

    private lateinit var searchCriteriaSpinner: Spinner
    private lateinit var searchQuery: EditText
    private lateinit var searchButton: Button
    private lateinit var showAllBooksButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var librosAdapter: LibrosAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_lib)

        searchCriteriaSpinner = findViewById(R.id.searchCriteriaSpinner)
        searchQuery = findViewById(R.id.searchQuery)
        searchButton = findViewById(R.id.searchButton)
        showAllBooksButton = findViewById(R.id.showAllBooksButton)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        librosAdapter = LibrosAdapter(emptyList())
        recyclerView.adapter = librosAdapter

        databaseHelper = DatabaseHelper(this)

        // Llama a populateSpinners para llenar el Spinner con opciones
        populateSpinners()

        searchButton.setOnClickListener {
            performSearch()
        }
        showAllBooksButton.setOnClickListener {
            showAllBooks()
        }
    }

    private fun performSearch() {
        val criteria = searchCriteriaSpinner.selectedItem.toString()
        val query = searchQuery.text.toString().trim()

        if (criteria == "Seleccione para buscar") {
            Toast.makeText(this, "Por favor, seleccione un criterio de búsqueda", Toast.LENGTH_SHORT).show()
            return
        }

        val results = when (criteria) {
            "Nombre" -> databaseHelper.searchBooks(query, "")
            "Género" -> databaseHelper.searchBooks("", query)
            else -> emptyList()
        }

        librosAdapter.updateData(results)
    }

    private fun populateSpinners() {
        val criteriaOptions = listOf("Seleccione opcion para buscar", "Nombre", "Género")
        val criteriaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, criteriaOptions)
        criteriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchCriteriaSpinner.adapter = criteriaAdapter
    }

    private fun showAllBooks() {
        val allBooks = databaseHelper.getAllBooks()  // Asegúrate de tener este método en DatabaseHelper
        librosAdapter.updateData(allBooks)
    }


}
