package com.example.cp06

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditarPeliculaActivity : AppCompatActivity() {
    private var peliculaId: Int = -1
    private var peliculaIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_pelicula)

        peliculaId = intent.getIntExtra("PELICULA_ID", -1)
        peliculaIndex = intent.getIntExtra("PELICULA_INDEX", -1)
        val nombrePelicula = intent.getStringExtra("PELICULA_NOMBRE") ?: ""

        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        editTextNombre.setText(nombrePelicula)

        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val nuevoNombre = editTextNombre.text.toString()

            val resultIntent = Intent().apply {
                putExtra("PELICULA_ID", peliculaId)
                putExtra("PELICULA_INDEX", peliculaIndex)
                putExtra("NUEVO_NOMBRE", nuevoNombre)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}