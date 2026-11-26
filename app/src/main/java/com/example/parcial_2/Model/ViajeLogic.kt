package com.example.parcial_2.Model

/**
 * Repositorio en memoria para la interfaz de viajes.
 * Los datos duran mientras el proceso de la aplicación siga activo.
 */
class ViajeLogic {
    private val viajes = mutableListOf<Viaje>()

    fun obtenerViajes(): List<Viaje> = viajes.toList()

    fun registrarViaje(viaje: Viaje) {
        viajes.add(viaje)
    }
}
