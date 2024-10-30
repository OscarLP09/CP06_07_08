package com.example.cp06

data class Pelicula(
    val id: Int,
    var nombre: String,
    val descripcion: String,
    val imagen: Int,  // Este será el ID del recurso de imagen (R.drawable)
    val duracion: Int,
    val ano: Int,
    val pais: String
)