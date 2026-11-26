package com.example.parcial_2.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_2.Model.Categoria
import com.example.parcial_2.Model.CreateExpenseRequest
import com.example.parcial_2.Model.CreateTripRequest
import com.example.parcial_2.Model.Gasto
import com.example.parcial_2.Model.RetrofitClient
import com.example.parcial_2.Model.Viaje
import com.example.parcial_2.Model.ViajeLogic
import kotlinx.coroutines.launch

class ViajesViewModel : ViewModel() {
    private val viajeLogic = ViajeLogic()
    private val api = RetrofitClient.api

    var viajes by mutableStateOf<List<Viaje>>(emptyList())
        private set

    var viajeSeleccionado by mutableStateOf<Viaje?>(null)
        private set

    var gastosViaje by mutableStateOf<List<Gasto>>(emptyList())
        private set

    var totalGastos by mutableStateOf(0.0)
        private set

    var categorias by mutableStateOf<List<Categoria>>(viajeLogic.obtenerCategorias())
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    init {
        cargarViajes()
        cargarCategorias()
    }

    fun cargarViajes() {
        viewModelScope.launch {
            runCatching {
                api.getTrips().map { dto ->
                    Viaje(
                        id = dto.id.toString(),
                        nombre = dto.name,
                        destino = dto.destination,
                        fechaInicio = dto.startDate,
                        fechaFin = dto.endDate
                    )
                }
            }.onSuccess { lista ->
                viajes = lista
            }.onFailure { e ->
                mensajeError = "Error al cargar viajes: ${e.message}"
            }
        }
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            runCatching {
                api.getCategories().map { dto ->
                    Categoria(id = dto.id.toString(), nombre = dto.name)
                }
            }.onSuccess { lista ->
                if (lista.isNotEmpty()) categorias = lista
            }.onFailure { e ->
                mensajeError = "Error al cargar categorías: ${e.message}"
            }
        }
    }

    fun registrarViaje(nombre: String, destino: String, fechaInicio: String, fechaFin: String): Boolean {
        val nombreLimpio = nombre.trim()
        val destinoLimpio = destino.trim()
        val error = viajeLogic.validarViaje(nombreLimpio, destinoLimpio, fechaInicio, fechaFin)
        if (error != null) {
            mensajeError = error
            return false
        }

        mensajeError = null
        viewModelScope.launch {
            runCatching {
                api.createTrip(
                    CreateTripRequest(
                        name = nombreLimpio,
                        destination = destinoLimpio,
                        startDate = fechaInicio,
                        endDate = fechaFin
                    )
                )
            }.onSuccess {
                cargarViajes()
            }.onFailure { e ->
                mensajeError = "Error al guardar viaje: ${e.message}"
            }
        }
        return true
    }

    fun seleccionarViaje(viaje: Viaje) {
        viajeSeleccionado = viaje
        mensajeError = null
        actualizarGastos()
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

        val tripIdInt = viaje.id.toIntOrNull()
        val categoryIdInt = categorias.find { it.nombre == categoria }?.id?.toIntOrNull()
        if (tripIdInt == null || categoryIdInt == null) {
            mensajeError = "No se encontró el ID del viaje o de la categoría en el servidor."
            return false
        }

        mensajeError = null
        viewModelScope.launch {
            runCatching {
                api.createExpense(
                    CreateExpenseRequest(
                        tripId = tripIdInt,
                        description = descripcionLimpia,
                        amount = valor,
                        categoryId = categoryIdInt,
                        expenseDate = fecha
                    )
                )
            }.onSuccess {
                actualizarGastos()
            }.onFailure { e ->
                mensajeError = "Error al guardar gasto: ${e.message}"
            }
        }
        return true
    }

    private fun actualizarGastos() {
        val tripIdInt = viajeSeleccionado?.id?.toIntOrNull() ?: return
        viewModelScope.launch {
            runCatching {
                api.getExpenses(tripIdInt).map { dto ->
                    Gasto(
                        id = dto.id.toString(),
                        viajeId = dto.tripId.toString(),
                        descripcion = dto.description,
                        valor = dto.amount,
                        categoria = dto.categoryName,
                        fecha = dto.expenseDate
                    )
                }
            }.onSuccess { lista ->
                gastosViaje = lista
                totalGastos = lista.sumOf { it.valor }
            }.onFailure { e ->
                mensajeError = "Error al cargar gastos: ${e.message}"
            }
        }
    }

    fun limpiarError() {
        mensajeError = null
    }
}
