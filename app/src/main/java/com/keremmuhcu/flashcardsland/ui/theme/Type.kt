package com.keremmuhcu.flashcardsland.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.keremmuhcu.flashcardsland.R


val openSansFontFamily = FontFamily(
    Font(R.font.opensans_light, FontWeight.Light),
    Font(R.font.opensans_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.opensans_bold, FontWeight.Bold),
    Font(R.font.opensans_bold_italic, FontWeight.Normal, FontStyle.Italic),
)

val AppTypography = Typography()