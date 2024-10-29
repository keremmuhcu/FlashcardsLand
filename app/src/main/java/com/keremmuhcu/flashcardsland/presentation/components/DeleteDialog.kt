package com.keremmuhcu.flashcardsland.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@Composable
fun DeleteDialog(
    title: String,
    text: String,
    isOpen: Boolean,
    onConfirm:() -> Unit,
    onCancel:() -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            title = {
                Text(text = title, fontFamily = gintoFontFamily)
            },
            text = {
                Text(text = text, fontFamily = gintoFontFamily)
            },
            onDismissRequest = { onCancel() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "Tamam", fontFamily = gintoFontFamily)
                }
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onCancel() }) {
                        Text(text = "İptal", fontFamily = gintoFontFamily)
                    }
                    TextButton(onClick = { onConfirm() }) {
                        Text(text = "Tamam", fontFamily = gintoFontFamily)
                    }
                }*/
            },
            dismissButton = {
                TextButton(onClick = { onCancel() }) {
                    Text(text = "İptal", fontFamily = gintoFontFamily)
                }
            }
        )
    }
}