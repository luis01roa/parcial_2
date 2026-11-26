package com.example.parcial_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial_2.View.CrearViajeScreen
import com.example.parcial_2.View.ViajesScreen
import com.example.parcial_2.ViewModel.ViajesViewModel
import com.example.parcial_2.ui.theme.Parcial_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Parcial_2Theme {
                val viajesViewModel: ViajesViewModel = viewModel()
                var mostrandoFormulario by remember { mutableStateOf(false) }

                if (mostrandoFormulario) {
                    CrearViajeScreen(
                        viewModel = viajesViewModel,
                        onVolver = { mostrandoFormulario = false },
                        onViajeGuardado = { mostrandoFormulario = false }
                    )
                } else {
                    ViajesScreen(
                        viewModel = viajesViewModel,
                        onCrearViaje = { mostrandoFormulario = true }
                    )
                }
            }
        }
    }
}
