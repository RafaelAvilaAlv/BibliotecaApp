package com.example.bibliotecaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UsuariosAdapter(private val usuarios: List<Persona>) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int = usuarios.size

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.textNombre1)
        private val apellido: TextView = itemView.findViewById(R.id.textApellido1)
        private val cedula: TextView = itemView.findViewById(R.id.textCedula1)
        private val correo: TextView = itemView.findViewById(R.id.textCorreo1)
        private val direccion: TextView = itemView.findViewById(R.id.textDireccion1)
        private val telefono: TextView = itemView.findViewById(R.id.textTelefono1)
        private val usuario: TextView = itemView.findViewById(R.id.textUsuario1)
        private val clave: TextView = itemView.findViewById(R.id.textClave1)
        private val foto: ImageView = itemView.findViewById(R.id.imageViewLibro) // Nota: Cambiado a imageView en lugar de textFoto1

        fun bind(persona: Persona) {
            nombre.text = persona.getNombre()
            apellido.text = persona.getApellido()
            cedula.text = persona.getCedula()
            correo.text = persona.getCorreo()
            direccion.text = persona.getDireccion()
            telefono.text = persona.getTelefono()
            usuario.text = persona.getUsuario()
            clave.text = persona.getClave()

            // Cargar la imagen usando Glide
            Glide.with(itemView.context)
                .load(persona.getFoto()) // Ruta de la imagen
                .placeholder(R.drawable.placeholder) // Imagen de marcador de posición
                .error(R.drawable.error) // Imagen de error
                .into(foto) // ImageView donde se cargará la imagen
        }
    }
}

