package com.example.bibliotecaapp

import android.os.Bundle
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
    }
}