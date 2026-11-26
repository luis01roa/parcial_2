package com.example.parcial_2.Model

data class Gasto (
    val id: String ="",
    val viajeId: String,
    val descripcion: String,
    val valor: Double,
    val categoria: String,
    val fecha: String
)