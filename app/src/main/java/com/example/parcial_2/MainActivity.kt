package com.example.parcial_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.parcial_2.View.CrearViajeScreen
import com.example.parcial_2.View.DetalleViajeScreen
import com.example.parcial_2.View.RegistrarGastoScreen
import com.example.parcial_2.View.ViajesScreen
import com.example.parcial_2.ViewModel.ViajesViewModel
import com.example.parcial_2.ui.theme.Parcial_2Theme

class MainActivity : ComponentActivity() {
    private val viajesViewModel: ViajesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Parcial_2Theme {
                var pantalla by remember { mutableIntStateOf(0) }

                when (pantalla) {
                    1 -> CrearViajeScreen(
                        viewModel = viajesViewModel,
                        onVolver = { pantalla = 0 },
                        onViajeGuardado = { pantalla = 0 }
                    )
                    2 -> DetalleViajeScreen(
                        viewModel = viajesViewModel,
                        onVolver = { pantalla = 0 },
                        onAgregarGasto = { pantalla = 3 }
                    )
                    3 -> RegistrarGastoScreen(
                        viewModel = viajesViewModel,
                        onVolver = { pantalla = 2 },
                        onGastoGuardado = { pantalla = 2 }
                    )
                    else -> ViajesScreen(
                        viewModel = viajesViewModel,
                        onCrearViaje = { pantalla = 1 },
                        onSeleccionarViaje = { viaje ->
                            viajesViewModel.seleccionarViaje(viaje)
                            pantalla = 2
                        }
                    )
                }
            }
        }
    }
}
