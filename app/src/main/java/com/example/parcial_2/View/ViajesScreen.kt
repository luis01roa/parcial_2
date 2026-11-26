package com.example.parcial_2.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.parcial_2.Model.Viaje
import com.example.parcial_2.ViewModel.ViajesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private fun dateFormatter(pattern: String) = SimpleDateFormat(pattern, Locale.forLanguageTag("es-CO")).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViajesScreen(viewModel: ViajesViewModel, onCrearViaje: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis viajes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearViaje) {
                Icon(Icons.Default.Add, contentDescription = "Crear viaje")
            }
        }
    ) { padding ->
        if (viewModel.viajes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Luggage, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(12.dp))
                Text("Aún no tienes viajes", style = MaterialTheme.typography.titleMedium)
                Text("Pulsa + para registrar el primero.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.viajes, key = { it.id }) { viaje ->
                    ViajeCard(viaje)
                }
            }
        }
    }
}

@Composable
private fun ViajeCard(viaje: Viaje) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Luggage, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(viaje.nombre, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                Text(viaje.destino, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "${formatearFecha(viaje.fechaInicio)} – ${formatearFecha(viaje.fechaFin)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearViajeScreen(
    viewModel: ViajesViewModel,
    onVolver: () -> Unit,
    onViajeGuardado: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf<Long?>(null) }
    var fechaFin by remember { mutableStateOf<Long?>(null) }
    var selectorFecha by remember { mutableStateOf(false) }
    var seleccionandoInicio by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear viaje") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; viewModel.limpiarError() },
                label = { Text("Nombre del viaje") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it; viewModel.limpiarError() },
                label = { Text("Destino") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FechaButton(
                    label = "Fecha de inicio",
                    fecha = fechaInicio,
                    modifier = Modifier.weight(1f)
                ) {
                    seleccionandoInicio = true
                    selectorFecha = true
                }
                FechaButton(
                    label = "Fecha de fin",
                    fecha = fechaFin,
                    modifier = Modifier.weight(1f)
                ) {
                    seleccionandoInicio = false
                    selectorFecha = true
                }
            }

            viewModel.mensajeError?.let { mensaje ->
                Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    val inicio = fechaInicio?.let(::fechaIso).orEmpty()
                    val fin = fechaFin?.let(::fechaIso).orEmpty()
                    if (viewModel.registrarViaje(nombre, destino, inicio, fin)) onViajeGuardado()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Luggage, contentDescription = null)
                Text("  Guardar")
            }
        }
    }

    if (selectorFecha) {
        SelectorFecha(
            fechaInicial = if (seleccionandoInicio) fechaInicio else fechaFin,
            onCancelar = { selectorFecha = false },
            onConfirmar = { millis ->
                if (seleccionandoInicio) fechaInicio = millis else fechaFin = millis
                selectorFecha = false
            }
        )
    }
}

@Composable
private fun FechaButton(label: String, fecha: Long?, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = fecha?.let(::formatearFecha).orEmpty(),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            placeholder = { Text("Seleccionar") },
            trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Text(if (fecha == null) "Elegir fecha" else "Cambiar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorFecha(
    fechaInicial: Long?,
    onCancelar: () -> Unit,
    onConfirmar: (Long) -> Unit
) {
    val state: DatePickerState = androidx.compose.material3.rememberDatePickerState(initialSelectedDateMillis = fechaInicial)
    DatePickerDialog(
        onDismissRequest = onCancelar,
        confirmButton = {
            TextButton(onClick = { state.selectedDateMillis?.let(onConfirmar) }) { Text("Aceptar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    ) {
        DatePicker(state = state, showModeToggle = false)
    }
}

private fun fechaIso(millis: Long): String = dateFormatter("yyyy-MM-dd").format(Date(millis))

private fun formatearFecha(fecha: String): String =
    runCatching { dateFormatter("dd MMM yyyy").format(dateFormatter("yyyy-MM-dd").parse(fecha)!!) }
        .getOrDefault(fecha)

private fun formatearFecha(millis: Long): String = dateFormatter("dd MMM yyyy").format(Date(millis))
