package com.example.parcial_2.Model

class ViajeLogic {

    fun calcularTotal(gastos: List<Gasto>): Double {
        return gastos.sumOf { it.valor }
    }

    fun validarViaje(nombre: String, destino: String, inicio: String, fin: String): String? {
        if (nombre.isBlank() || destino.isBlank() || inicio.isBlank() || fin.isBlank()) {
            return "Todos los campos son obligatorios"
        }
        if (fin < inicio) {
            return "La fecha de fin no puede ser menor a la de inicio"
        }
        return null
    }

    fun validarGasto(
        descripcion: String,
        valor: Double,
        categoria: String,
        fecha: String,
        fechaInicioViaje: String,
        fechaFinViaje: String
    ): String? {
        if (descripcion.isBlank() || categoria.isBlank() || fecha.isBlank()) {
            return "Todos los campos son obligatorios"
        }
        if (valor <= 0) {
            return "El valor debe ser positivo"
        }
        if (fecha < fechaInicioViaje || fecha > fechaFinViaje) {
            return "La fecha debe estar dentro del período del viaje"
        }
        return null
    }
}