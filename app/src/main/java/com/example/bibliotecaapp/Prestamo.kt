package com.example.bibliotecaapp

data class Prestamo(
    val id: Int = 0,
    val usuario_id: Int,
    val libro_id: Int,
    val fecha_prestamo: String,
    val fecha_devolucion: String

)