package com.prachaarbot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


@Composable
fun PracharBotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val color = lightColors(
        primary = Yellow50,
        primaryVariant = Yellow80,
        secondary = Black,
        surface = Yellow50
    )
    MaterialTheme(
        colors = color,
        typography = Typography,
        content = content
    )
}