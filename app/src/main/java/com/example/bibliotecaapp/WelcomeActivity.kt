package com.example.bibliotecaapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var buttonverinfo: Button
    private lateinit var buttonprestamo: Button
    private lateinit var buttonverlibros: Button
    private lateinit var buttonverprestamos: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        buttonverinfo = findViewById(R.id.buttonverinfo)
        buttonprestamo = findViewById(R.id.buttonprestamo)
        buttonverlibros= findViewById(R.id.buttonverlibros)
        buttonverprestamos= findViewById(R.id.buttonverprestamos)



        buttonverinfo.setOnClickListener{
            showResulPerfil()
        }
        buttonprestamo.setOnClickListener{
            showResultpedido()
        }
        buttonverlibros.setOnClickListener{
            showResultBusclibro()
        }
        buttonverprestamos.setOnClickListener{
            showResultHistorial()
        }

    }
    //Abrir Perfil
    private fun showResulPerfil() {
        val intent = Intent(this,PerfilUsuario::class.java).apply {

        }
        startActivity(intent)
    }

    //Abrir pedir libros
    private fun showResultpedido() {
        val intent = Intent(this, AgegarPrestamo::class.java).apply {

        }
        startActivity(intent)
    }

    //BuscarLibro
    private fun showResultBusclibro() {
        val intent = Intent(this, BuscarLib::class.java).apply {


        }
        startActivity(intent)
    }
    //Historial prestamos
    private fun showResultHistorial() {
        val intent = Intent(this, HistorialPresUsuario::class.java).apply {

        }
        startActivity(intent)
    }

}