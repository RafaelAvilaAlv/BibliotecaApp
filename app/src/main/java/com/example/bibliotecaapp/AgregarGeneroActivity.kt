package com.example.bibliotecaapp


import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarGeneroActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var btnGuardarGenero: Button
    private lateinit var btnListarGenero: Button
    private lateinit var btnActualizarGenero: Button
    private lateinit var btnBorrarGenero: Button
    lateinit var codigo: EditText
    private lateinit var listaMensaje: TextView
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_genero)

        etNombre = findViewById(R.id.etNombre)
        codigo = findViewById(R.id.codigo)
        btnGuardarGenero = findViewById(R.id.btnGuardarGenero)
        btnListarGenero = findViewById(R.id.btnListarGenero)
        btnActualizarGenero = findViewById(R.id.btnActualizarGenero) // Asegúrate de tener este botón en tu layout
        btnBorrarGenero = findViewById(R.id.btnBorrarGenero) // Asegúrate de tener este botón en tu layout
        listaMensaje = findViewById(R.id.listaMensaje)

        btnGuardarGenero.setOnClickListener { guardarGenero() }
        btnListarGenero.setOnClickListener { listarGeneros() }
        btnActualizarGenero.setOnClickListener { actualizarGenero() }
        btnBorrarGenero.setOnClickListener { borrarGenero() }

        btnRegresar = findViewById(R.id.btnRegresar)


        // Configurar el botón para que termine la actividad cuando se presione
        btnRegresar.setOnClickListener {
            finish()  // Termina la actividad y regresa a la actividad anterior
        }
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
            etNombre.text.clear()
        } else {
            Toast.makeText(this, "Error al guardar el género", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    private fun listarGeneros() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "genero",
            arrayOf("genero_id", "nombre"),
            null, null, null, null, null
        )

        val generos = StringBuilder()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("genero_id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))

                generos.append("ID: $id\n")
                generos.append("Nombre: $nombre\n\n")
            }
        }
        cursor.close()

        listaMensaje.text = generos.toString()
    }

    private fun actualizarGenero() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val id = codigo.text.toString()
        val nombre = etNombre.text.toString()

        if (id.isNotEmpty() && nombre.isNotEmpty()) {
            val values = ContentValues().apply {
                put("nombre", nombre)
            }

            val rowsAffected = db.update(
                "genero",
                values,
                "genero_id = ?",
                arrayOf(id)
            )

            if (rowsAffected > 0) {
                Toast.makeText(this, "Género actualizado correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar el género.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarGenero() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val id = codigo.text.toString()

        if (id.isNotEmpty()) {
            val rowsDeleted = db.delete(
                "genero",
                "genero_id = ?",
                arrayOf(id)
            )

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Género eliminado correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar el género.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        } else {
            Toast.makeText(this, "Por favor, complete el campo de ID.", Toast.LENGTH_SHORT).show()
        }
    }
}
