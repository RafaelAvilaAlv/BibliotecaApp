package com.example.bibliotecaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistorialPedidosAdapter(private val prestamos: List<Prestamo>, private val obtenerTituloLibro: (Int) -> String) :
    RecyclerView.Adapter<HistorialPedidosAdapter.PrestamoViewHolder>() {

    class PrestamoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTituloPedido: TextView = itemView.findViewById(R.id.textViewTituloPedido)
        val textViewFechaPedido: TextView = itemView.findViewById(R.id.textViewFechaPedido)
        val textViewFechaDevolucion: TextView = itemView.findViewById(R.id.textViewFechaDevolucion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestamoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial_pedido, parent, false)
        return PrestamoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PrestamoViewHolder, position: Int) {
        val prestamo = prestamos[position]
        holder.textViewTituloPedido.text = obtenerTituloLibro(prestamo.libro_id)
        holder.textViewFechaPedido.text = "Fecha de Pedido: ${prestamo.fecha_prestamo}"
        holder.textViewFechaDevolucion.text = "Fecha de Devolución: ${prestamo.fecha_devolucion}"
    }

    override fun getItemCount() = prestamos.size
}
