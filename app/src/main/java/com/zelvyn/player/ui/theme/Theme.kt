package com.zelvyn.player.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF07090E)
val SurfaceDark = Color(0xFF10141E)
val SurfaceBorder = Color(0xFF1F2637)
val AccentViolet = Color(0xFF7C3AED)
val AccentCyan = Color(0xFF00E5FF)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)

val ZelvynColorScheme = darkColorScheme(
    primary = AccentViolet,
    secondary = AccentCyan,
    background = DarkBackground,
    surface = SurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)
