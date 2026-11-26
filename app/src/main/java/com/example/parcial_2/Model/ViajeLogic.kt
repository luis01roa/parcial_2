package com.example.parcial_2.Model

class ViajeLogic {
    private val viajes = mutableListOf<Viaje>()
    private val gastos = mutableListOf<Gasto>()
    private val categorias = listOf(
        Categoria("1", "Alimentación"),
        Categoria("2", "Alojamiento"),
        Categoria("3", "Transporte"),
        Categoria("4", "Entretenimiento"),
        Categoria("5", "Otros")
    )

    fun obtenerViajes(): List<Viaje> = viajes.toList()

    fun obtenerViajePorId(id: String): Viaje? = viajes.find { it.id == id }

    fun registrarViaje(viaje: Viaje) {
        viajes.add(viaje)
    }

    fun obtenerCategorias(): List<Categoria> = categorias

    fun obtenerGastosPorViaje(viajeId: String): List<Gasto> =
        gastos.filter { it.viajeId == viajeId }

    fun registrarGasto(gasto: Gasto) {
        gastos.add(gasto)
    }

    fun calcularTotal(viajeId: String): Double =
        gastos.filter { it.viajeId == viajeId }.sumOf { it.valor }

    fun validarViaje(
        nombre: String,
        destino: String,
        fechaInicio: String,
        fechaFin: String
    ): String? {
        if (nombre.isBlank() || destino.isBlank() || fechaInicio.isBlank() || fechaFin.isBlank()) {
            return "Completa todos los campos obligatorios."
        }
        if (fechaFin < fechaInicio) {
            return "La fecha final debe ser igual o posterior a la fecha de inicio."
        }
        return null
    }

    fun validarGasto(
        descripcion: String,
        valor: Double,
        categoria: String,
        fecha: String,
        viaje: Viaje
    ): String? {
        if (descripcion.isBlank() || categoria.isBlank() || fecha.isBlank()) {
            return "Completa todos los campos obligatorios."
        }
        if (valor <= 0.0) {
            return "El valor del gasto debe ser mayor a 0."
        }
        if (fecha < viaje.fechaInicio || fecha > viaje.fechaFin) {
            return "La fecha del gasto debe estar dentro del período del viaje."
        }
        return null
    }
}