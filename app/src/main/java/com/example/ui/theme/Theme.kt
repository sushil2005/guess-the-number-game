package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = IndigoLight,
    onPrimary = SlateDarkBg,
    primaryContainer = IndigoPrimary,
    onPrimaryContainer = Color.White,
    secondary = IndigoLight,
    onSecondary = SlateDarkBg,
    background = SlateDarkBg,
    onBackground = TextLightMain,
    surface = CardDarkBg,
    onSurface = TextLightMain,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = TextLightMuted,
    outline = Color(0xFF475569)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = IndigoLight,
    onPrimaryContainer = IndigoDark,
    secondary = IndigoDark,
    onSecondary = Color.White,
    background = SlateBg,
    onBackground = TextDarkMain,
    surface = Color.White,
    onSurface = TextDarkMain,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextDarkMuted,
    outline = Color(0xFFE2E8F0)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
