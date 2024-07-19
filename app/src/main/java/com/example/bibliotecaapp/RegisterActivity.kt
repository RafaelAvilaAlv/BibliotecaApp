package com.example.bibliotecaapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCedula: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etDireccion: EditText
    private lateinit var ettelefono: EditText
    private lateinit var etUsuario: EditText
    private lateinit var etClave: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCedula= findViewById(R.id.etCedula)
        etCorreo= findViewById(R.id.etCorreo)
        etDireccion= findViewById(R.id.etDireccion)
        ettelefono= findViewById(R.id.ettelefono)
        etUsuario = findViewById(R.id.etUsuario)
        etClave = findViewById(R.id.etClave)

        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnRegresar = findViewById(R.id.btnRegresar)

        btnRegistrar.setOnClickListener { registrarUsuario() }
        btnRegresar.setOnClickListener { regresar() }
    }

    private fun registrarUsuario() {
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val cedula= etCedula.text.toString()
        val correo= etCorreo.text.toString()
        val direccion= etDireccion.text.toString()
        val telefono=ettelefono.text.toString()
        val usuario = etUsuario.text.toString()
        val clave = etClave.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || correo.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("cedula", cedula)
            put("correo", correo)
            put("direccion", direccion)
            put("telefono", telefono)
            put("usuario", usuario)
            put("clave", clave)
        }

        val newRowId = db.insert("usuarios", null, values)

        if (newRowId != -1L) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    private fun regresar() {
        finish()
    }
}