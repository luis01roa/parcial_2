package com.example.parcial_2

import com.example.parcial_2.Model.Gasto
import com.example.parcial_2.Model.Viaje
import com.example.parcial_2.Model.ViajeLogic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ViajeLogicTest {

    private lateinit var logic: ViajeLogic
    private lateinit var viajeEuropa: Viaje
    private lateinit var viajeSantaMarta: Viaje

    @Before
    fun setUp() {
        logic = ViajeLogic()
        viajeEuropa = Viaje(
            id = "1",
            nombre = "Europa 2026",
            destino = "Italia",
            fechaInicio = "2026-06-10",
            fechaFin = "2026-06-20"
        )
        viajeSantaMarta = Viaje(
            id = "2",
            nombre = "Santa Marta",
            destino = "Colombia",
            fechaInicio = "2026-07-01",
            fechaFin = "2026-07-05"
        )
    }

    // --- Validación de viajes ---

    @Test
    fun validarViaje_datosValidos_retornaNull() {
        val error = logic.validarViaje("Europa 2026", "Italia", "2026-06-10", "2026-06-20")
        assertNull(error)
    }

    @Test
    fun validarViaje_camposVacios_retornaError() {
        assertNotNull(logic.validarViaje("", "Italia", "2026-06-10", "2026-06-20"))
        assertNotNull(logic.validarViaje("Europa 2026", "   ", "2026-06-10", "2026-06-20"))
        assertNotNull(logic.validarViaje("Europa 2026", "Italia", "", "2026-06-20"))
        assertNotNull(logic.validarViaje("Europa 2026", "Italia", "2026-06-10", ""))
    }

    @Test
    fun validarViaje_fechaFinAnteriorAInicio_retornaError() {
        val error = logic.validarViaje("Europa 2026", "Italia", "2026-06-20", "2026-06-10")
        assertNotNull(error)
    }

    // --- Validación de gastos ---

    @Test
    fun validarGasto_datosValidos_retornaNull() {
        val error = logic.validarGasto(
            descripcion = "Cena en Trastevere",
            valor = 28.50,
            categoria = "Alimentación",
            fecha = "2026-06-12",
            viaje = viajeEuropa
        )
        assertNull(error)
    }

    @Test
    fun validarGasto_camposVacios_retornaError() {
        assertNotNull(logic.validarGasto("", 10.0, "Alimentación", "2026-06-12", viajeEuropa))
        assertNotNull(logic.validarGasto("Cena", 10.0, "", "2026-06-12", viajeEuropa))
        assertNotNull(logic.validarGasto("Cena", 10.0, "Alimentación", "", viajeEuropa))
    }

    @Test
    fun validarGasto_valorCeroONegativo_retornaError() {
        assertNotNull(logic.validarGasto("Cena", 0.0, "Alimentación", "2026-06-12", viajeEuropa))
        assertNotNull(logic.validarGasto("Cena", -50.0, "Alimentación", "2026-06-12", viajeEuropa))
    }

    @Test
    fun validarGasto_fechaAnteriorAlInicioDelViaje_retornaError() {
        val error = logic.validarGasto("Vuelo", 100.0, "Transporte", "2026-06-09", viajeEuropa)
        assertNotNull(error)
    }

    @Test
    fun validarGasto_fechaPosteriorAlFinDelViaje_retornaError() {
        val error = logic.validarGasto("Hotel", 100.0, "Alojamiento", "2026-06-21", viajeEuropa)
        assertNotNull(error)
    }

    // --- Registro, consulta, suma y aislamiento por viaje ---

    @Test
    fun registrarYConsultarViajes_funcionaCorrectamente() {
        logic.registrarViaje(viajeEuropa)
        logic.registrarViaje(viajeSantaMarta)

        assertEquals(2, logic.obtenerViajes().size)
        assertEquals("Europa 2026", logic.obtenerViajePorId("1")?.nombre)
        assertNull(logic.obtenerViajePorId("999"))
    }

    @Test
    fun obtenerCategorias_devuelveListaNoVacia() {
        val categorias = logic.obtenerCategorias()
        assertTrue(categorias.isNotEmpty())
        assertTrue(categorias.any { it.nombre == "Alimentación" })
    }

    @Test
    fun calcularTotal_sumaSoloLosGastosDelViajeConsultado() {
        logic.registrarViaje(viajeEuropa)
        logic.registrarViaje(viajeSantaMarta)

        logic.registrarGasto(Gasto("g1", "1", "Cena", 28.50, "Alimentación", "2026-06-12"))
        logic.registrarGasto(Gasto("g2", "1", "Hotel", 260.00, "Alojamiento", "2026-06-13"))
        logic.registrarGasto(Gasto("g3", "1", "Bus", 7.00, "Transporte", "2026-06-14"))
        logic.registrarGasto(Gasto("g4", "2", "Lancha", 120.00, "Transporte", "2026-07-02"))

        assertEquals(295.50, logic.calcularTotal("1"), 0.001)
        assertEquals(120.00, logic.calcularTotal("2"), 0.001)
        assertEquals(0.0, logic.calcularTotal("999"), 0.001)

        assertEquals(3, logic.obtenerGastosPorViaje("1").size)
        assertEquals(1, logic.obtenerGastosPorViaje("2").size)
    }
}

