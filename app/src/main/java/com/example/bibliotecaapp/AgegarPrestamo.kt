package com.example.bibliotecaapp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AgegarPrestamo : AppCompatActivity(){
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewAutor: TextView
    private lateinit var textViewGenero: TextView
    private lateinit var editTextFechaPedido: EditText
    private lateinit var editTextFechaDevolucion: EditText
    private lateinit var buttonInsertarPedido: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_prestamo)

        // Inicializar los elementos del layout
        textViewTitulo = findViewById(R.id.textViewTitulo)
        textViewAutor = findViewById(R.id.textViewAutor)
        textViewGenero = findViewById(R.id.textViewGenero)
        editTextFechaPedido = findViewById(R.id.editTextFechaPedido)
        editTextFechaDevolucion = findViewById(R.id.editTextFechaDevolucion)
        buttonInsertarPedido = findViewById(R.id.buttonInsertarPedido)

        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Obtener el ID del libro desde el Intent
        val bookId = intent.getIntExtra("BOOK_ID", -1)
        if (bookId != -1) {
            // Usar el ID del libro para obtener detalles o realizar acciones
            val libro = databaseHelper.getBookById(bookId)
            if (libro != null) {
                // Actualizar la UI con los detalles del libro
                textViewTitulo.text = libro.titulo
                textViewAutor.text = libro.autorNombre
                textViewGenero.text = libro.generoNombre
            } else {
                // Manejar el caso en el que el libro no se encuentra
                textViewTitulo.text = "Libro no encontrado"
                textViewAutor.text = ""
                textViewGenero.text = ""
            }
        } else {
            // Manejar el caso en el que no se pasó un ID válido
            textViewTitulo.text = "ID de libro no válido"
            textViewAutor.text = ""
            textViewGenero.text = ""
        }

        // Configurar el selector de fecha
        val calendar = Calendar.getInstance()

        val dateSetListenerPedido = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            editTextFechaPedido.setText(String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth))
        }
        editTextFechaPedido.setOnClickListener {
            DatePickerDialog(this, dateSetListenerPedido, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val dateSetListenerDevolucion = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            editTextFechaDevolucion.setText(String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth))
        }
        editTextFechaDevolucion.setOnClickListener {
            DatePickerDialog(this, dateSetListenerDevolucion, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Configurar el botón para insertar el pedido
        buttonInsertarPedido.setOnClickListener {
            val fechaPedido = editTextFechaPedido.text.toString()
            val fechaDevolucion = editTextFechaDevolucion.text.toString()

            if (fechaPedido.isNotEmpty() && fechaDevolucion.isNotEmpty()) {
                val prestamo = Prestamo(
                    usuario_id = 1, // Reemplaza con el ID del usuario correspondiente
                    libro_id = bookId,
                    fecha_prestamo = fechaPedido,
                    fecha_devolucion = fechaDevolucion
                )
                insertPedido(prestamo)
                Toast.makeText(this, "Pedido insertado exitosamente", Toast.LENGTH_SHORT).show()
                finish() // Finaliza la actividad y regresa a la anterior
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertPedido(prestamo: Prestamo) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put("usuario_id", prestamo.usuario_id)
            put("libro_id", prestamo.libro_id)
            put("fecha_prestamo", prestamo.fecha_prestamo)
            put("fecha_devolucion", prestamo.fecha_devolucion)
        }
        db.insert("prestamo", null, values)
    }
}
