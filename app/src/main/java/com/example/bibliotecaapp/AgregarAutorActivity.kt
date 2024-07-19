package com.example.bibliotecaapp


import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarAutorActivity : AppCompatActivity() {
    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText

    private lateinit var etFechaNacimiento: EditText
    private lateinit var etPaisId: EditText
    private lateinit var btnGuardarAutor: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_autor)

        etNombres = findViewById(R.id.etNombres)
        etApellidos = findViewById(R.id.etApellidos)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etPaisId = findViewById(R.id.etPais)
        btnGuardarAutor = findViewById(R.id.btnGuardarAutor)

        btnGuardarAutor.setOnClickListener { guardarAutor() }
    }

    private fun guardarAutor() {
        val nombres = etNombres.text.toString()
        val apellidos = etApellidos.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val pais = etPaisId.text.toString()

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
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el autor", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }
}
