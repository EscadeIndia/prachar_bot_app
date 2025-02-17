package com.prachaarbot.ui.theme

import androidx.compose.material.SliderDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.prachaarbot.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

internal val Segoe = FontFamily(
    Font(R.font.segoe_regular, FontWeight.Normal),
    Font(R.font.segoe_bold, FontWeight.Bold),
    Font(R.font.segoe_italic, FontWeight.Thin)
)

internal val Monstserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semi_bold, FontWeight.SemiBold)
)

@Composable
fun TextFieldDefaults.getTextFieldDefault() = TextFieldDefaults.textFieldColors(
    textColor = Black,
    disabledTextColor = Grey,
    backgroundColor = Color.White,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    cursorColor = Color.Black,
)

@Composable
fun SliderDefaults.getDefaultColors() = SliderDefaults.colors(
    thumbColor = Color(0xFFFFC600),
    activeTrackColor = Color(0xFFFFC600),
    inactiveTrackColor = Color(0xFFDCDCDC)
)


