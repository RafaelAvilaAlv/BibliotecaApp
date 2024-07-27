package com.example.bibliotecaapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LAdapter(private var librosList: List<Libros>) :
    RecyclerView.Adapter<LAdapter.LibrosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_librousuario, parent, false)

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
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewLibro)
        private val requestButton: Button = itemView.findViewById(R.id.btn_request)

        fun bind(libro: Libros) {
            tituloTextView.text = libro.titulo
            autorTextView.text = libro.autorNombre
            generoTextView.text = libro.generoNombre

            // Cargar la imagen usando Glide
            Glide.with(itemView.context)
                .load(libro.imagePath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            // Manejar el clic del botón de pedido
            requestButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, AgegarPrestamo::class.java)
                intent.putExtra("BOOK_ID", libro.id_libro)
                context.startActivity(intent)
            }
        }
    }


}
