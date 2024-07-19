package com.example.bibliotecaapp

class Persona {
    private var nombre: String? = null
    private var apellido: String? = null
    private var cedula: String? = null
    private var correo: String? = null
    private var direccion: String? = null
    private var telefono: String? = null
    private var usuario: String? = null
    private var clave: String? = null

    fun Persona(nombre: String?, apellido: String?,cedula: String?,correo: String?,direccion: String?,telefono: String?, usuario: String?, clave: String?) {
        this.nombre = nombre
        this.apellido = apellido
        this.cedula= cedula
        this.correo= correo
        this.direccion=direccion
        this.telefono= telefono
        this.usuario = usuario
        this.clave = clave
    }

    fun getNombre(): String? {
        return nombre
    }

    fun getApellido(): String? {
        return apellido
    }
    fun getCedula(): String?{
        return cedula
    }

    fun getCorreo(): String?{
        return correo
    }

    fun getDireccion(): String?{
        return direccion
    }

    fun getTelefono(): String?{
        return telefono
    }

    fun getUsuario(): String? {
        return usuario
    }

    fun getClave(): String? {
        return clave
    }
}