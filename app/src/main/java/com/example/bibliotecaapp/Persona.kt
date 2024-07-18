package com.example.bibliotecaapp

class Persona {
    private var nombre: String? = null
    private var apellido: String? = null
    private var usuario: String? = null
    private var clave: String? = null

    fun Persona(nombre: String?, apellido: String?, usuario: String?, clave: String?) {
        this.nombre = nombre
        this.apellido = apellido
        this.usuario = usuario
        this.clave = clave
    }

    fun getNombre(): String? {
        return nombre
    }

    fun getApellido(): String? {
        return apellido
    }

    fun getUsuario(): String? {
        return usuario
    }

    fun getClave(): String? {
        return clave
    }
}