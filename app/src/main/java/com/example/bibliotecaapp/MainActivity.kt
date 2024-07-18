package com.example.bibliotecaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var etUsuario: EditText
    private lateinit var etClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.etUsuario)
        etClave = findViewById(R.id.etClave)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener { login() }
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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