package com.example.bibliotecaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class PerfilUsuario : AppCompatActivity() {
    private lateinit var tvNombre: TextView
    private lateinit var tvApellido: TextView
    private lateinit var tvCedula: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var tvUsuario: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        dbHelper = DatabaseHelper(this)

        tvNombre = findViewById(R.id.tvNombre)
        tvApellido = findViewById(R.id.tvApellido)
        tvCedula = findViewById(R.id.tvCedula)
        tvCorreo = findViewById(R.id.tvCorreo)
        tvDireccion = findViewById(R.id.tvDireccion)
        tvTelefono = findViewById(R.id.tvTelefono)
        tvUsuario = findViewById(R.id.tvUsuario)

        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        // Obtener las preferencias compartidas
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val correo = sharedPref.getString("user_email", null)


        Log.d("PerfilUsuario", "Correo recuperado: $correo")

        if (correo != null) {
            val db = dbHelper.readableDatabase

            // Consultar los datos del usuario basado en el correo
            val cursor = db.query(
                "usuarios", // Asegúrate de que este nombre coincida con el nombre de la tabla en la base de datos
                arrayOf("nombre", "apellido", "cedula", "correo", "direccion", "telefono", "usuario"),
                "correo = ?",
                arrayOf(correo),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                // Mostrar los datos del usuario en los TextViews
                tvNombre.text = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                tvApellido.text = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                tvCedula.text = cursor.getString(cursor.getColumnIndexOrThrow("cedula"))
                tvCorreo.text = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                tvDireccion.text = cursor.getString(cursor.getColumnIndexOrThrow("direccion"))
                tvTelefono.text = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
                tvUsuario.text = cursor.getString(cursor.getColumnIndexOrThrow("usuario"))
            } else {
                // Manejo del caso cuando no se encuentra el usuario
                tvNombre.text = "Usuario no encontrado"
                tvApellido.text = "N/A"
                tvCedula.text = "N/A"
                tvCorreo.text = "N/A"
                tvDireccion.text = "N/A"
                tvTelefono.text = "N/A"
                tvUsuario.text = "N/A"
            }

            cursor.close()
            db.close()
        } else {
            // Manejo del caso cuando el correo es nulo
            tvNombre.text = "Correo no encontrado en preferencias compartidas."
            tvApellido.text = "N/A"
            tvCedula.text = "N/A"
            tvCorreo.text = "N/A"
            tvDireccion.text = "N/A"
            tvTelefono.text = "N/A"
            tvUsuario.text = "N/A"
        }
    }

    private fun guardarCorreoUsuario(correo: String) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_email", correo)
            apply()
        }
    }



}

