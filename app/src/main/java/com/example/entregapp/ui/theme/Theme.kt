package com.example.entregapp.ui.theme

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

private val FallbackLightColorScheme = lightColorScheme(
    primary = PetroleumBlue,
    onPrimary = Color.White,
    primaryContainer = PetroleumBlueContainer,
    onPrimaryContainer = OnPetroleumBlueContainer,
    secondary = SageGreen,
    onSecondary = Color.White,
    secondaryContainer = SageGreenContainer,
    onSecondaryContainer = OnSageGreenContainer,
    tertiary = SageGreenDark,
    onTertiary = Color.White,
    background = AppBackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceLight,
    outline = OutlineLight
)

private val FallbackDarkColorScheme = darkColorScheme(
    primary = PetroleumBlueLight,
    onPrimary = PetroleumBlueDark,
    primaryContainer = PetroleumBlue,
    onPrimaryContainer = Color.White,
    secondary = SageGreenLight,
    onSecondary = SageGreenDark,
    secondaryContainer = SageGreen,
    onSecondaryContainer = Color.White,
    tertiary = SageGreenLight,
    onTertiary = SageGreenDark,
    background = AppBackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceDark,
    outline = OutlineDark
)

@Composable
fun EntregappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> FallbackDarkColorScheme
        else -> FallbackLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}