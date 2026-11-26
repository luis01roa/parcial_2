package com.example.parcial_2.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.parcial_2.Model.Gasto
import com.example.parcial_2.Model.Viaje
import com.example.parcial_2.Model.ViajeLogic
import java.util.UUID

class ViajesViewModel : ViewModel() {
    private val viajeLogic = ViajeLogic()

    var viajes by mutableStateOf(viajeLogic.obtenerViajes())
        private set

    var viajeSeleccionado by mutableStateOf<Viaje?>(null)
        private set

    var gastosViaje by mutableStateOf(emptyList<Gasto>())
        private set

    var totalGastos by mutableStateOf(0.0)
        private set

    val categorias = viajeLogic.obtenerCategorias()

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun registrarViaje(nombre: String, destino: String, fechaInicio: String, fechaFin: String): Boolean {
        val nombreLimpio = nombre.trim()
        val destinoLimpio = destino.trim()
        val error = viajeLogic.validarViaje(nombreLimpio, destinoLimpio, fechaInicio, fechaFin)
        if (error != null) {
            mensajeError = error
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

    fun seleccionarViaje(viaje: Viaje) {
        viajeSeleccionado = viaje
        actualizarGastos()
        mensajeError = null
    }

    fun registrarGasto(
        descripcion: String,
        valor: Double,
        categoria: String,
        fecha: String
    ): Boolean {
        val viaje = viajeSeleccionado
        if (viaje == null) {
            mensajeError = "Selecciona un viaje antes de registrar un gasto."
            return false
        }

        val descripcionLimpia = descripcion.trim()
        val error = viajeLogic.validarGasto(
            descripcion = descripcionLimpia,
            valor = valor,
            categoria = categoria,
            fecha = fecha,
            viaje = viaje
        )
        if (error != null) {
            mensajeError = error
            return false
        }

        viajeLogic.registrarGasto(
            Gasto(
                id = UUID.randomUUID().toString(),
                viajeId = viaje.id,
                descripcion = descripcionLimpia,
                valor = valor,
                categoria = categoria,
                fecha = fecha
            )
        )
        actualizarGastos()
        mensajeError = null
        return true
    }

    private fun actualizarGastos() {
        val viajeId = viajeSeleccionado?.id ?: return
        gastosViaje = viajeLogic.obtenerGastosPorViaje(viajeId)
        totalGastos = viajeLogic.calcularTotal(viajeId)
    }

    fun limpiarError() {
        mensajeError = null
    }
}
