package com.example.parcial_2.View

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.style.TextOverflow
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
fun ViajesScreen(
    viewModel: ViajesViewModel,
    onCrearViaje: () -> Unit,
    onSeleccionarViaje: (Viaje) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis viajes", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearViaje,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        if (viewModel.viajes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("✈", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(16.dp))
                        Text("Aún no tienes viajes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Pulsa + para planificar tu primer viaje.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(viewModel.viajes, key = { it.id }) { viaje ->
                    ViajeCard(viaje, onClick = { onSeleccionarViaje(viaje) })
                }
            }
        }
    }
}

@Composable
private fun ViajeCard(viaje: Viaje, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                    Text("✈", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                }
            }
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(
                    viaje.nombre,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                title = { Text("Crear viaje", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Text("‹", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                "Planea tu próxima aventura",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
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
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar viaje", fontWeight = FontWeight.SemiBold)
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
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("▦", color = MaterialTheme.colorScheme.primary)
            Text(
                fecha?.let(::formatearFecha) ?: "Elegir fecha",
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectorFecha(
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

internal fun fechaIso(millis: Long): String = dateFormatter("yyyy-MM-dd").format(Date(millis))

internal fun formatearFecha(fecha: String): String =
    runCatching { dateFormatter("dd MMM yyyy").format(dateFormatter("yyyy-MM-dd").parse(fecha)!!) }
        .getOrDefault(fecha)

internal fun formatearFecha(millis: Long): String = dateFormatter("dd MMM yyyy").format(Date(millis))
