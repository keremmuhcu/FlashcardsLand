package com.keremmuhcu.flashcardsland.presentation.study.basic.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun ResultTextRow(
    text: String,
    result: String,
    color: Color = Color.Unspecified
) {
    val textStyle = TextStyle(
        fontSize = 22.sp,
        fontFamily = openSansFontFamily,
    )
    Row {
        Text(
            text = text,
            style = textStyle,
            color = color
        )
        Text(
            text = result,
            style = textStyle
        )
    }
}