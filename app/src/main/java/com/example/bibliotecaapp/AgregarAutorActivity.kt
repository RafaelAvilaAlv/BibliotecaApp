package com.example.bibliotecaapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarAutorActivity : AppCompatActivity() {
    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etPais: EditText
    private lateinit var etAutorId: EditText // Campo para el ID del autor
    private lateinit var btnGuardarAutor: Button
    private lateinit var btnListarAutor: Button
    private lateinit var btnActualizarAutor: Button
    private lateinit var btnBorrarAutor: Button
    private lateinit var listaMensaje: TextView
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_autor)

        etNombres = findViewById(R.id.etNombres)
        etApellidos = findViewById(R.id.etApellidos)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etPais = findViewById(R.id.etPais)
        etAutorId = findViewById(R.id.codigo) // Asegúrate de tener este campo en tu layout
        btnGuardarAutor = findViewById(R.id.btnGuardarAutor)
        btnListarAutor = findViewById(R.id.btnListarAutor)
        btnActualizarAutor = findViewById(R.id.btnActualizarAutor) // Asegúrate de tener este botón en tu layout
        btnBorrarAutor = findViewById(R.id.btnBorrarAutor) // Asegúrate de tener este botón en tu layout
        listaMensaje = findViewById(R.id.listaMensaje)

        btnGuardarAutor.setOnClickListener { guardarAutor() }
        btnListarAutor.setOnClickListener { listarAutores() }
        btnActualizarAutor.setOnClickListener { actualizarAutor() }
        btnBorrarAutor.setOnClickListener { borrarAutor() }

        btnRegresar = findViewById(R.id.btnRegresar)

        // Configurar el botón para que termine la actividad cuando se presione
        btnRegresar.setOnClickListener {
            finish()  // Termina la actividad y regresa a la actividad anterior
        }
    }

    private fun guardarAutor() {
        val nombres = etNombres.text.toString()
        val apellidos = etApellidos.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val pais = etPais.text.toString()

        if (nombres.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty() || pais.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombres", nombres)
            put("apellidos", apellidos)
            put("fecha_nacimiento", fechaNacimiento)
            put("pais", pais)
        }

        val newRowId = db.insert("autor", null, values)

        if (newRowId != -1L) {
            Toast.makeText(this, "Autor guardado exitosamente", Toast.LENGTH_SHORT).show()
            etNombres.text.clear()
            etApellidos.text.clear()
            etFechaNacimiento.text.clear()
            etPais.text.clear()
        } else {
            Toast.makeText(this, "Error al guardar el autor", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    private fun listarAutores() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "autor",
            arrayOf("autor_id", "nombres", "apellidos", "fecha_nacimiento", "pais"),
            null, null, null, null, null
        )

        val autores = StringBuilder()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("autor_id"))
                val nombres = getString(getColumnIndexOrThrow("nombres"))
                val apellidos = getString(getColumnIndexOrThrow("apellidos"))
                val fechaNacimiento = getString(getColumnIndexOrThrow("fecha_nacimiento"))
                val pais = getString(getColumnIndexOrThrow("pais"))

                autores.append("ID: $id\n")
                autores.append("Nombres: $nombres\n")
                autores.append("Apellidos: $apellidos\n")
                autores.append("Fecha de Nacimiento: $fechaNacimiento\n")
                autores.append("País: $pais\n\n")
            }
        }
        cursor.close()

        listaMensaje.text = autores.toString()
    }

    private fun actualizarAutor() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val id = etAutorId.text.toString()
        val nombres = etNombres.text.toString()
        val apellidos = etApellidos.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val pais = etPais.text.toString()

        if (id.isNotEmpty() && nombres.isNotEmpty() && apellidos.isNotEmpty() && fechaNacimiento.isNotEmpty() && pais.isNotEmpty()) {
            val values = ContentValues().apply {
                put("nombres", nombres)
                put("apellidos", apellidos)
                put("fecha_nacimiento", fechaNacimiento)
                put("pais", pais)
            }

            val rowsAffected = db.update(
                "autor",
                values,
                "autor_id = ?",
                arrayOf(id)
            )

            if (rowsAffected > 0) {
                Toast.makeText(this, "Autor actualizado correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar el autor.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarAutor() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val id = etAutorId.text.toString()

        if (id.isNotEmpty()) {
            val rowsDeleted = db.delete(
                "autor",
                "autor_id = ?",
                arrayOf(id)
            )

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Autor eliminado correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar el autor.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        } else {
            Toast.makeText(this, "Por favor, complete el campo de ID.", Toast.LENGTH_SHORT).show()
        }
    }
}
