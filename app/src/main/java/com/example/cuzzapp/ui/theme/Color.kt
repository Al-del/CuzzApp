package com.example.cuzzapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LighterRed = Color(0xFFA9A9A9)
val Pink = Color(0xFFFEFDED)
val LightOrange = Color(0xFFC6EBC5)
val LightYellow = Color(0xFFDC5F00)
private val DarkColorPalette = darkColors(
    primary = Color(0xFFCF0A0A),
    primaryVariant = Color(0xFFEEEEEE),
    secondary = Color(0xFFDC5F00),
    background = Color(0xFF262323) // Add this line
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFFA7070),
    primaryVariant = Color(0xFFC6EBC5),
    secondary = Color(0xFFA1C398),
    background = Color(0xFFFEFDED) // Add this line
)

@Composable
fun CuzzAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {


}