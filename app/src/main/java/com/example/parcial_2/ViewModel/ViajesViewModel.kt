package com.example.parcial_2.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.parcial_2.Model.Viaje
import com.example.parcial_2.Model.ViajeLogic
import java.util.UUID

class ViajesViewModel : ViewModel() {
    private val viajeLogic = ViajeLogic()

    var viajes by mutableStateOf(viajeLogic.obtenerViajes())
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun registrarViaje(nombre: String, destino: String, fechaInicio: String, fechaFin: String): Boolean {
        val nombreLimpio = nombre.trim()
        val destinoLimpio = destino.trim()

        if (nombreLimpio.isEmpty() || destinoLimpio.isEmpty() || fechaInicio.isBlank() || fechaFin.isBlank()) {
            mensajeError = "Completa todos los campos."
            return false
        }
        if (fechaFin < fechaInicio) {
            mensajeError = "La fecha final debe ser posterior a la fecha de inicio."
            return false
        }

        viajeLogic.registrarViaje(
            Viaje(
                id = UUID.randomUUID().toString(),
                nombre = nombreLimpio,
                destino = destinoLimpio,
                fechaInicio = fechaInicio,
                fechaFin = fechaFin
            )
        )
        viajes = viajeLogic.obtenerViajes()
        mensajeError = null
        return true
    }

    fun limpiarError() {
        mensajeError = null
    }
}
