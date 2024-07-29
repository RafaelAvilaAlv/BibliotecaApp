package com.example.bibliotecaapp



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bibliotecaapp.Libros
//import com.example.bibliotecaapp.Libros
//import com.example.librarymanagerapp.Libros
import com.example.bibliotecaapp.R

class LibrosAdapter(private var librosList: List<Libros>) :
    RecyclerView.Adapter<LibrosAdapter.LibrosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibrosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LibrosViewHolder, position: Int) {
        val currentLibro = librosList[position]
        holder.bind(currentLibro)
    }

    override fun getItemCount() = librosList.size

    fun updateData(newLibrosList: List<Libros>) {
        librosList = newLibrosList
        notifyDataSetChanged()
    }

    inner class LibrosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloTextView: TextView = itemView.findViewById(R.id.textViewTitulo)
        private val autorTextView: TextView = itemView.findViewById(R.id.textViewAutor)
        private val generoTextView: TextView = itemView.findViewById(R.id.textViewGenero)
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewLibro) // Añade ImageView aquí

        fun bind(libro: Libros) {
            tituloTextView.text = libro.titulo
            autorTextView.text = libro.autorNombre
            generoTextView.text = libro.generoNombre

            // Cargar la imagen usando Glide
            Glide.with(itemView.context)
                .load(libro.imagePath) // Ruta de la imagen
                .placeholder(R.drawable.placeholder) // Imagen de marcador de posición
                .error(R.drawable.error) // Imagen de error
                .into(imageView) // ImageView donde se cargará la imagen
        }
    }
}
