package com.example.bibliotecaapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var etUsuario: EditText
    private lateinit var etClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnTogglePassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.etUsuario)
        etClave = findViewById(R.id.etClave)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        btnTogglePassword = findViewById(R.id.btnTogglePassword) // Aquí añadimos el botón de mostrar/ocultar contraseña

        btnLogin.setOnClickListener { login() }
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Aquí se añade la funcionalidad para mostrar/ocultar la contraseña
        btnTogglePassword.setOnClickListener {
            if (etClave.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                etClave.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePassword.text = "Mostrar"
            } else {
                etClave.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnTogglePassword.text = "Ocultar"
            }
            etClave.setSelection(etClave.text.length)
        }
    }

    private fun login() {
        val usuario = etUsuario.text.toString()
        val clave = etClave.text.toString()

        if (usuario == "admin" && clave == "1234") {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        } else {
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND clave = ?", arrayOf(usuario, clave))
            if (cursor.moveToFirst()) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Usuario o clave incorrectos", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
            db.close()
        }
    }
}
