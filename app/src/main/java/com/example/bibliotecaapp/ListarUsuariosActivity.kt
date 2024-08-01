package com.example.bibliotecaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListarUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var usuariosAdapter: UsuariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_usuarios)

        recyclerView = findViewById(R.id.recyclerViewUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        val usuarios = dbHelper.getAllUsuarios()

        usuariosAdapter = UsuariosAdapter(usuarios)
        recyclerView.adapter = usuariosAdapter

        // Configurar el botón para generar el reporte
        val buttonGenerateReport: Button = findViewById(R.id.buttonGenerateReport)
        buttonGenerateReport.setOnClickListener {
            val intent = Intent(this, ReporteUsuariosActivity::class.java)
            startActivity(intent)
        }
    }

}