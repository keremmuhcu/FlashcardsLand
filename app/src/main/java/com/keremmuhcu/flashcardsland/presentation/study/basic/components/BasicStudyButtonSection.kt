package com.keremmuhcu.flashcardsland.presentation.study.basic.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun BasicStudyButtonSection(
    onWrongButtonClick:() -> Unit,
    onCorrectButtonClick:() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Button(
            modifier = Modifier.weight(0.5f),
            onClick = { onWrongButtonClick() },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(text = "Yanlış", fontFamily = openSansFontFamily, fontWeight = FontWeight.SemiBold)
        }
        Button(
            modifier = Modifier.weight(0.5f),
            onClick = { onCorrectButtonClick() },
            shape = RectangleShape,

            ) {
            Text(text = "Doğru", fontFamily = openSansFontFamily, fontWeight = FontWeight.SemiBold)
        }
    }
}