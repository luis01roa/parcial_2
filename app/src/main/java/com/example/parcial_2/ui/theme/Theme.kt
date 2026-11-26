package com.example.parcial_2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ViajesColorScheme = lightColorScheme(
    primary = Verde,
    onPrimary = Blanco,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeOscuro,
    secondary = VerdeOscuro,
    onSecondary = Blanco,
    secondaryContainer = VerdeClaro,
    onSecondaryContainer = VerdeOscuro,
    background = VerdeSuave,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    surfaceVariant = VerdeClaro,
    onSurfaceVariant = TextoSecundario,
    error = Error,
    onError = Blanco
)

@Composable
fun Parcial_2Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ViajesColorScheme,
        typography = Typography,
        content = content
    )
}
