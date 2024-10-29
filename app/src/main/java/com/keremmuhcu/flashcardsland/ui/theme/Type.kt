package com.keremmuhcu.flashcardsland.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.keremmuhcu.flashcardsland.R


val gintoFontFamily = FontFamily(
    Font(R.font.ginto_regular, FontWeight.Normal),
    Font(R.font.ginto_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.ginto_light, FontWeight.Light),
    Font(R.font.ginto_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.ginto_medium, FontWeight.Medium),
    Font(R.font.ginto_bold, FontWeight.Bold)
)

val AppTypography = Typography()