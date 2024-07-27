package com.example.bibliotecaapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WelcomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var lAdapter: LAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        databaseHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadBooks()


        // Navegación por los ítems
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    showResulPerfil()
                }
                R.id.nav_request_books -> {
                    showResultpedido()
                }
                R.id.nav_search_books -> {
                    showResultBusclibro()
                }
                R.id.nav_loan_history -> {
                    showResultHistorial()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }



    // Abrir Perfil
    private fun showResulPerfil() {
        val intent = Intent(this, PerfilUsuario::class.java)
        startActivity(intent)
    }

    // Abrir pedir libros
    private fun showResultpedido() {
        val intent = Intent(this, AgregarPrestamo::class.java)
        startActivity(intent)
    }

    // Buscar Libro
    private fun showResultBusclibro() {
        val intent = Intent(this, BuscarLib::class.java)
        startActivity(intent)
    }

    // Historial préstamos
    private fun showResultHistorial() {
        val intent = Intent(this, HistorialPresUsuario::class.java)
        startActivity(intent)
    }


    private fun loadBooks() {
        val books = databaseHelper.getAllBooks()
        lAdapter = LAdapter(books)
        recyclerView.adapter = lAdapter
    }
}

class AgregarPrestamo : AppCompatActivity() {
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewAutor: TextView
    private lateinit var textViewGenero: TextView
    private lateinit var editTextFechaPedido: EditText
    private lateinit var editTextFechaDevolucion: EditText
    private lateinit var buttonInsertarPedido: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_prestamo)

        // Inicializar los elementos del layout
        textViewTitulo = findViewById(R.id.textViewTitulo)
        textViewAutor = findViewById(R.id.textViewAutor)
        textViewGenero = findViewById(R.id.textViewGenero)
        editTextFechaPedido = findViewById(R.id.editTextFechaPedido)
        editTextFechaDevolucion = findViewById(R.id.editTextFechaDevolucion)
        buttonInsertarPedido = findViewById(R.id.buttonInsertarPedido)

        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Obtener el ID del libro desde el Intent
        val bookId = intent.getIntExtra("BOOK_ID", -1)
        if (bookId != -1) {
            // Usar el ID del libro para obtener detalles o realizar acciones
            val libro = databaseHelper.getBookById(bookId)
            if (libro != null) {
                // Actualizar la UI con los detalles del libro
                textViewTitulo.text = libro.titulo
                textViewAutor.text = libro.autorNombre
                textViewGenero.text = libro.generoNombre
            } else {
                // Manejar el caso en el que el libro no se encuentra
                textViewTitulo.text = "Libro no encontrado"
                textViewAutor.text = ""
                textViewGenero.text = ""
            }
        } else {
            // Manejar el caso en el que no se pasó un ID válido
            textViewTitulo.text = "ID de libro no válido"
            textViewAutor.text = ""
            textViewGenero.text = ""
        }

        // Configurar el botón para insertar el pedido
        buttonInsertarPedido.setOnClickListener {
            val fechaPedido = editTextFechaPedido.text.toString()
            val fechaDevolucion = editTextFechaDevolucion.text.toString()

            if (fechaDevolucion.isNotEmpty()) {
                val prestamo = Prestamo(
                    usuario_id = 1, // Reemplaza con el ID del usuario correspondiente
                    libro_id = bookId,
                    fecha_prestamo = fechaPedido,
                    fecha_devolucion = fechaDevolucion
                )
                insertPedido(prestamo)
                Toast.makeText(this, "Pedido insertado exitosamente", Toast.LENGTH_SHORT).show()
                finish() // Finaliza la actividad y regresa a la anterior
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertPedido(prestamo: Prestamo) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put("usuario_id", prestamo.usuario_id)
            put("libro_id", prestamo.libro_id)
            put("fecha_prestamo", prestamo.fecha_prestamo)
            put("fecha_devolucion", prestamo.fecha_devolucion)
        }
        db.insert("pedidos", null, values)
    }

}

