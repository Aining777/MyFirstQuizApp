package com.example.dailyquizapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 定义暗色主题的颜色方案
// 这些颜色（Purple80, PurpleGrey80, Pink80）需要在 Color.kt 中定义
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// 定义亮色主题的颜色方案
// 这些颜色（Purple40, PurpleGrey40, Pink40）也需要在 Color.kt 中定义
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* 其他可以覆盖的默认颜色，例如：
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * 每日测验应用的主题 Composable 函数。
 * 它根据系统主题（亮色/暗色）和Android版本（是否支持动态颜色）来选择颜色方案，
 * 并设置状态栏颜色。
 *
 * @param darkTheme 是否使用暗色主题，默认为系统设置。
 * @param dynamicColor 是否启用动态颜色（仅限 Android 12+），默认为 true。
 * @param content 要应用主题的 UI 内容。
 */
@Composable
fun DailyQuizAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // 默认启用动态颜色
    content: @Composable () -> Unit
) {
    // 根据主题和Android版本选择颜色方案
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    // 如果不在预览模式下，设置状态栏颜色
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // 应用 MaterialTheme，传入颜色方案、排版和内容
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // 这里引用了 Type.kt 中定义的 Typography
        content = content
    )
}
