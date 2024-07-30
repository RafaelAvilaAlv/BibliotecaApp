package com.example.bibliotecaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    private lateinit var botonlibros: Button
    private lateinit var botonautores: Button
    private lateinit var botongenero: Button
    private lateinit var botonUsuarios: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        botonlibros = findViewById(R.id.buttonLibros)
        botonautores = findViewById(R.id.buttonAutores)
        botongenero = findViewById(R.id.buttonGeneros)
        botonUsuarios = findViewById(R.id.buttonUsuarios)

        botonlibros.setOnClickListener {
            showResultlibro()
        }

        botonautores.setOnClickListener {
            showResultaurores()
        }

        botongenero.setOnClickListener {
            showResultgenero()
        }

        botonUsuarios.setOnClickListener {
            showResultUsuarios()
        }
    }

    // Abrir autores
    private fun showResultaurores() {
        val intent = Intent(this, AgregarAutorActivity::class.java)
        startActivity(intent)
    }

    // Abrir genero
    private fun showResultgenero() {
        val intent = Intent(this, AgregarGeneroActivity::class.java)
        startActivity(intent)
    }

    // Abrir libro
    private fun showResultlibro() {
        val intent = Intent(this, AgregarLibros::class.java)
        startActivity(intent)
    }

    // Abrir listado de usuarios
    private fun showResultUsuarios() {
        val intent = Intent(this, ListarUsuariosActivity::class.java)
        startActivity(intent)
    }
}