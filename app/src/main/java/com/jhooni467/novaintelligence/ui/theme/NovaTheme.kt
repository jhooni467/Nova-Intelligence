package com.jhooni467.novaintelligence.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object NovaColors {
    val Background = Color(0xFF090811)
    val Surface = Color(0xFF171329)
    val SurfaceRaised = Color(0xFF211B35)
    val Purple = Color(0xFFA88BFF)
    val PurpleBright = Color(0xFFC7B5FF)
    val TextPrimary = Color(0xFFF5F1FF)
    val TextSecondary = Color(0xFFAAA2BC)
    val Border = Color(0xFF342B49)
}

@Composable
fun NovaTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = NovaColors.Purple,
        onPrimary = NovaColors.Background,
        secondary = NovaColors.PurpleBright,
        background = NovaColors.Background,
        onBackground = NovaColors.TextPrimary,
        surface = NovaColors.Surface,
        onSurface = NovaColors.TextPrimary,
        surfaceVariant = NovaColors.SurfaceRaised,
        onSurfaceVariant = NovaColors.TextSecondary
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
