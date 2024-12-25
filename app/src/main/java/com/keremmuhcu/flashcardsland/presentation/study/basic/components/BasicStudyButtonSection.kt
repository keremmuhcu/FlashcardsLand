package com.keremmuhcu.flashcardsland.presentation.study.basic.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BasicStudyButtonSection(
    onWrongButtonClick: () -> Unit,
    onCorrectButtonClick: () -> Unit
) {
    var isButtonsEnabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    fun handleButtonClick(action: () -> Unit) {
        if (isButtonsEnabled) {
            isButtonsEnabled = false
            action()
            scope.launch {
                delay(600) // Animation süresi kadar bekle
                isButtonsEnabled = true
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            modifier = Modifier.weight(0.5f),
            onClick = { handleButtonClick(onWrongButtonClick) },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = MaterialTheme.colorScheme.error
            ),
            enabled = isButtonsEnabled
        ) {
            Text(text = "Yanlış", fontFamily = openSansFontFamily, fontWeight = FontWeight.SemiBold)
        }
        Button(
            modifier = Modifier.weight(0.5f),
            onClick = { handleButtonClick(onCorrectButtonClick) },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.primary,
            ),
            enabled = isButtonsEnabled
        ) {
            Text(text = "Doğru", fontFamily = openSansFontFamily, fontWeight = FontWeight.SemiBold)
        }
    }
}