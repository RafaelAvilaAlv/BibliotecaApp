package com.example.bibliotecaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    private lateinit var botonlibros: Button
    private lateinit var botonautores: Button
    private lateinit var botongenero: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        botonlibros = findViewById(R.id.buttonLibros)
        botonautores = findViewById(R.id.buttonAutores)
        botongenero= findViewById(R.id.buttonGeneros)

       // botonlibros.setOnClickListener{
         //   showResult()
        //}

         botonautores.setOnClickListener{
             showResultaurores()
         }

        botongenero.setOnClickListener{
            showResultgenero()
        }





    }
    //Abrir autores
    private fun showResultaurores() {
        val intent = Intent(this, AgregarAutorActivity::class.java).apply {

        }
        startActivity(intent)
    }

//Abrir genero
    private fun showResultgenero() {
        val intent = Intent(this, AgregarGeneroActivity::class.java).apply {

        }
        startActivity(intent)
    }


}