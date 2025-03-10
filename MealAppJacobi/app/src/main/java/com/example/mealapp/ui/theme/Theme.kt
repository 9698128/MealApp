package com.example.mealapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define custom colors
val LightRed = Color(0xDEB11E1E)  // rosso scuro
val LightGreen = Color(0xFF4CAF50)  // Light green for buttons
val BackgroundWhite = Color(0xFFFFFFFF)  // Pure white background

private val ColorScheme = lightColorScheme(
    primary = LightRed,
    secondary = LightRed,
    tertiary = LightGreen,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    primaryContainer = Color(0xDEB11E1E),  // Very light red for containers
    secondaryContainer = Color(0xDEB11E1E),
    tertiaryContainer = Color(0xFFEAFFEA)  // Very light green for button containers
)

@Composable
fun MealAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}