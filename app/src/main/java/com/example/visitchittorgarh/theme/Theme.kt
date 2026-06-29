package com.example.visitchittorgarh.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = SaffronPrimary,
    secondary = CrimsonSecondary,
    tertiary = GoldAccent,
    background = SlateBackgroundDark,
    surface = SlateSurfaceDark,
    onPrimary = SlateBackgroundDark,
    onSecondary = TextLight,
    onTertiary = TextDark,
    onBackground = TextLight,
    onSurface = TextLight
  )

private val LightColorScheme =
  lightColorScheme(
    primary = CrimsonSecondary,
    secondary = SaffronPrimary,
    tertiary = GoldAccent,
    background = SlateBackgroundLight,
    surface = SlateSurfaceLight,
    onPrimary = Color.White,
    onSecondary = TextLight,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark
  )

@Composable
fun VisitChittorgarhTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Saffron/Crimson theme is preferred
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

