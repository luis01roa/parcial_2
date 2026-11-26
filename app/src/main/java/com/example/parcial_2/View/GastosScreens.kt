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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.parcial_2.Model.Viaje
import com.example.parcial_2.ViewModel.ViajesViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.Box
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleViajeScreen(
    viewModel: ViajesViewModel,
    onVolver: () -> Unit,
    onAgregarGasto: () -> Unit
) {
    val viaje = viewModel.viajeSeleccionado ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del viaje", fontWeight = FontWeight.SemiBold) },
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
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = onAgregarGasto,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(viaje.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(viaje.destino, style = MaterialTheme.typography.titleMedium)
            Text(
                "${formatearFecha(viaje.fechaInicio)} – ${formatearFecha(viaje.fechaFin)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
                    Text("TOTAL DEL VIAJE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(
                        viewModel.totalGastos.aMoneda(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                "Gastos registrados · ${viewModel.gastosViaje.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (viewModel.gastosViaje.isEmpty()) {
                Text("Este viaje aún no tiene gastos.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.gastosViaje, key = { it.id }) { gasto ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(gasto.descripcion, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        "${gasto.categoria} · ${formatearFecha(gasto.fecha)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(gasto.valor.aMoneda(), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarGastoScreen(
    viewModel: ViajesViewModel,
    onVolver: () -> Unit,
    onGastoGuardado: () -> Unit
) {
    val viaje: Viaje = viewModel.viajeSeleccionado ?: return
    var descripcion by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf<Long?>(null) }
    var mostrarCategorias by remember { mutableStateOf(false) }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar gasto", fontWeight = FontWeight.SemiBold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Viaje: ${viaje.nombre}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Agrega un gasto a tu viaje", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it; viewModel.limpiarError() },
                label = { Text("Descripción") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it; viewModel.limpiarError() },
                label = { Text("Valor") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    placeholder = { Text("Selecciona una categoría") },
                    trailingIcon = { Text("⌄") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { mostrarCategorias = true }
                )
                DropdownMenu(
                    expanded = mostrarCategorias,
                    onDismissRequest = { mostrarCategorias = false }
                ) {
                    viewModel.categorias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.nombre) },
                            onClick = {
                                categoria = opcion.nombre
                                mostrarCategorias = false
                                viewModel.limpiarError()
                            }
                        )
                    }
                }
            }
            OutlinedButton(
                onClick = { mostrarSelectorFecha = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("▦", color = MaterialTheme.colorScheme.primary)
                Text(
                    fecha?.let { formatearFecha(it) } ?: "Seleccionar fecha del gasto",
                    modifier = Modifier.padding(start = 10.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "La fecha debe estar entre ${formatearFecha(viaje.fechaInicio)} y ${formatearFecha(viaje.fechaFin)}.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            viewModel.mensajeError?.let { mensaje ->
                Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    val valorNumerico = valor.trim().replace(',', '.').toDoubleOrNull() ?: 0.0
                    val fechaIso = fecha?.let(::fechaIso).orEmpty()
                    if (viewModel.registrarGasto(descripcion, valorNumerico, categoria, fechaIso)) {
                        onGastoGuardado()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar gasto", fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (mostrarSelectorFecha) {
        SelectorFecha(
            fechaInicial = fecha,
            onCancelar = { mostrarSelectorFecha = false },
            onConfirmar = { millis ->
                fecha = millis
                mostrarSelectorFecha = false
                viewModel.limpiarError()
            }
        )
    }
}

private fun Double.aMoneda(): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(this)
