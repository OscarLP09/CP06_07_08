package com.example.cp06

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class MainActivity : AppCompatActivity() {
    private lateinit var peliculaAdapter: PeliculaAdapter
    private var peliculasTemporales = mutableListOf<Pelicula>() // Lista original con todas las películas
    private var peliculasFiltradas = mutableListOf<Pelicula>()   // Lista que muestra resultados filtrados
    private var posicionSeleccionada: Int = -1 // Variable para guardar la posición seleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar la lista original y la lista filtrada
        peliculasTemporales = PeliculaProvider.listaCarga.toMutableList()
        peliculasFiltradas.addAll(peliculasTemporales) // Comienza mostrando todas las películas

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar el adaptador para utilizar la lista filtrada
        peliculaAdapter = PeliculaAdapter(peliculasFiltradas, { pelicula ->
            // Mostrar Toast con información de la película al hacer clic
            Toast.makeText(this, "Duración: ${pelicula.duracion} minutos – Año: ${pelicula.ano}", Toast.LENGTH_SHORT).show()
        }, { posicion ->
            // Guardar la posición seleccionada y abrir el menú contextual para editar o eliminar
            posicionSeleccionada = posicion
            registerForContextMenu(recyclerView)
            recyclerView.showContextMenu()
        })

        recyclerView.adapter = peliculaAdapter
    }

    // Configuración del menú con el SearchView
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        // Configuración del listener de texto en el SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // No se requiere acción adicional al enviar
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarPeliculas(newText ?: "")
                return true
            }
        })
        return true
    }


    // Filtrar películas en tiempo real
    private fun filtrarPeliculas(query: String) {
        peliculasFiltradas.clear() // Limpiar la lista filtrada

        if (query.isEmpty()) {
            // Si no hay texto, muestra todas las películas
            peliculasFiltradas.addAll(peliculasTemporales)
        } else {
            // Filtra las películas que contengan el texto en el nombre
            peliculasFiltradas.addAll(peliculasTemporales.filter { pelicula ->
                pelicula.nombre.lowercase().contains(query.lowercase())
            })
        }

        // Notificar al adaptador de los cambios en los datos
        peliculaAdapter.notifyDataSetChanged()
    }

    // Manejar opciones de menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_recargar -> {
                peliculasFiltradas.clear()
                peliculasFiltradas.addAll(PeliculaProvider.listaCarga)
                peliculaAdapter.notifyDataSetChanged()
                true
            }
            R.id.action_limpiar -> {
                AlertDialog.Builder(this)
                    .setTitle("Eliminar todas las películas")
                    .setMessage("¿Estás seguro de que deseas eliminar todas las películas de la lista?")
                    .setPositiveButton("Sí") { dialog, which ->
                        peliculasFiltradas.clear()
                        peliculaAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Menú contextual para editar o eliminar una película
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.context_eliminar -> {
                AlertDialog.Builder(this)
                    .setTitle("Eliminar película")
                    .setMessage("¿Estás seguro de que deseas eliminar esta película?")
                    .setPositiveButton("Sí") { dialog, which ->
                        peliculasFiltradas.removeAt(posicionSeleccionada)
                        peliculaAdapter.notifyItemRemoved(posicionSeleccionada)
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.context_editar -> {
                val peliculaSeleccionada = peliculasFiltradas[posicionSeleccionada]
                val intent = Intent(this, EditarPeliculaActivity::class.java).apply {
                    putExtra("PELICULA_ID", peliculaSeleccionada.id)
                    putExtra("PELICULA_NOMBRE", peliculaSeleccionada.nombre)
                    putExtra("PELICULA_INDEX", posicionSeleccionada)
                }
                startActivityForResult(intent, REQUEST_EDIT_PELICULA)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_PELICULA && resultCode == RESULT_OK) {
            val peliculaIndex = data?.getIntExtra("PELICULA_INDEX", -1) ?: -1
            val nuevoNombre = data?.getStringExtra("NUEVO_NOMBRE") ?: ""

            if (peliculaIndex != -1) {
                peliculasTemporales[peliculaIndex].nombre = nuevoNombre
                peliculasFiltradas.find { it.id == peliculasTemporales[peliculaIndex].id }?.nombre = nuevoNombre
                peliculaAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val REQUEST_EDIT_PELICULA = 1
    }
}