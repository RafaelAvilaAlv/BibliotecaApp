package com.example.bibliotecaapp

data class Libros (
    val id_libro: Int,
    val titulo: String,
    val autor_id:Int,
    val genero_id:Int,

    //ES NECESARIO PARA QUE DEVUELVA LOS DATOS

    val autorNombre: String = "", // Agrega esto
    val generoNombre: String = "",// Agrega esto
    val imagePath: String? // Agregar esta propiedad



)