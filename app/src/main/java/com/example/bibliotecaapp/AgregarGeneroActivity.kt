package com.example.bibliotecaapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarGeneroActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var btnGuardarGenero: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_genero)

        etNombre = findViewById(R.id.etNombre)
        btnGuardarGenero = findViewById(R.id.btnGuardarGenero)

        btnGuardarGenero.setOnClickListener { guardarGenero() }
    }

    private fun guardarGenero() {
        val nombre = etNombre.text.toString()

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, complete el campo de nombre", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", nombre)
        }

        val newRowId = db.insert("genero", null, values)

        if (newRowId != -1L) {
            Toast.makeText(this, "Género guardado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el género", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }
}
