package com.example.bibliotecaapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var etCorreo: EditText
    private lateinit var etClave: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etCorreo = findViewById(R.id.etCorreo)
        etClave = findViewById(R.id.etClave)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val correo = etCorreo.text.toString()
        val clave = etClave.text.toString()

        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "usuarios",
            arrayOf("correo", "clave"),
            "correo = ? AND clave = ?",
            arrayOf(correo, clave),
            null,
            null,
            null
        )

        if (cursor.count > 0) {
            // Guardar correo en SharedPreferences
            val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("user_email", correo)
                apply()
            }

            cursor.close()
            db.close()

            // Ir a la pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Correo o clave incorrectos", Toast.LENGTH_SHORT).show()
            cursor.close()
            db.close()
        }
    }
}
