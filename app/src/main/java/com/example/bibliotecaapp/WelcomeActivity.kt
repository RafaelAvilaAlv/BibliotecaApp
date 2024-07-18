package com.example.bibliotecaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    private var tvWelcome: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tvWelcome = findViewById(R.id.tvWelcome)

        val usuario = intent.getStringExtra("usuario")
        tvWelcome?.text = "Hola $usuario"
    }
}