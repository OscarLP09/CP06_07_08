package com.example.cp06

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PeliculaAdapter(
    var peliculas: MutableList<Pelicula>,
    val onClick: (Pelicula) -> Unit,
    val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return PeliculaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val pelicula = peliculas[position]
        holder.bind(pelicula, position, onClick, onLongClick)
    }

    override fun getItemCount(): Int = peliculas.size

    class PeliculaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pelicula: Pelicula, position: Int, onClick: (Pelicula) -> Unit, onLongClick: (Int) -> Unit) {
            itemView.findViewById<TextView>(R.id.titulo).text = pelicula.nombre
            itemView.findViewById<ImageView>(R.id.imagen).setImageResource(pelicula.imagen)

            // Evento de clic corto
            itemView.setOnClickListener { onClick(pelicula) }

            // Evento de clic largo, ahora pasamos la posición
            itemView.setOnLongClickListener {
                onLongClick(position)
                true
            }
        }
    }
}