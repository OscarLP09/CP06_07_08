package com.example.cp06

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallePeliculaActivity : AppCompatActivity() {
    private lateinit var tituloTextView: TextView
    private lateinit var descripcionTextView: TextView
    private lateinit var duracionTextView: TextView
    private lateinit var anoTextView: TextView
    private lateinit var paisTextView: TextView
    private lateinit var imagenImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pelicula)

        // Inicializar views
        tituloTextView = findViewById(R.id.titulo)
        descripcionTextView = findViewById(R.id.descripcion)
        duracionTextView = findViewById(R.id.duracion)
        anoTextView = findViewById(R.id.ano)
        paisTextView = findViewById(R.id.pais)
        imagenImageView = findViewById(R.id.imagen)

        // Obtener los datos de la película que se pasan en el Intent
        val peliculaId = intent.getIntExtra("PELICULA_ID", -1)
        val pelicula = PeliculaProvider.listaCarga.find { it.id == peliculaId }

        // Mostrar los datos de la película
        pelicula?.let {
            tituloTextView.text = it.nombre
            descripcionTextView.text = it.descripcion
            duracionTextView.text = "Duración: ${it.duracion} minutos"
            anoTextView.text = "Año: ${it.ano}"
            paisTextView.text = "País: ${it.pais}"
            imagenImageView.setImageResource(it.imagen)
        }
    }
}
