package com.vedicvidya.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Saffron,
    onPrimary = White,
    secondary = Gold,
    onSecondary = DarkBrown,
    tertiary = DeepBlue,
    background = DarkBrown,
    onBackground = Cream,
    surface = MediumBrown,
    onSurface = Cream,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = Saffron,
    onPrimary = White,
    secondary = DeepSaffron,
    onSecondary = White,
    tertiary = RoyalBlue,
    background = LightCream,
    onBackground = DarkBrown,
    surface = Cream,
    onSurface = DarkBrown,
    error = Error
)

@Composable
fun VedicVidyaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
