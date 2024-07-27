package com.example.bibliotecaapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialPresUsuario : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: HistorialPedidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_pres_usuario)

        recyclerView = findViewById(R.id.recyclerViewHistorial)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)

        // Obtener el ID del usuario desde el Intent
        val userId = intent.getIntExtra("USER_ID", -1)

        if (userId != -1) {
            // Obtener el historial de préstamos para el usuario
            val prestamos = databaseHelper.getPrestamosByUserId(userId)

            // Configurar el adaptador del RecyclerView
            adapter = HistorialPedidosAdapter(prestamos) { libroId ->
                databaseHelper.getLibroTitleById(libroId)
            }
            recyclerView.adapter = adapter
        } else {
            // Manejar el caso en el que no se pasó un ID válido
            // (puedes mostrar un mensaje de error, etc.)
        }
    }
}