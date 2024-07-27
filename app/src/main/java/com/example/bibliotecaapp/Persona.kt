package com.example.bibliotecaapp

class Persona(
    private var nombre: String?,
    private var apellido: String?,
    private var cedula: String?,
    private var correo: String?,
    private var direccion: String?,
    private var telefono: String?,
    private var usuario: String?,
    private var clave: String?,
    private var foto: String?
) {

    fun getNombre(): String? {
        return nombre
    }

    fun getApellido(): String? {
        return apellido
    }

    fun getCedula(): String? {
        return cedula
    }

    fun getCorreo(): String? {
        return correo
    }

    fun getDireccion(): String? {
        return direccion
    }

    fun getTelefono(): String? {
        return telefono
    }

    fun getUsuario(): String? {
        return usuario
    }

    fun getClave(): String? {
        return clave
    }

    fun getFoto(): String? {
        return foto
    }
}