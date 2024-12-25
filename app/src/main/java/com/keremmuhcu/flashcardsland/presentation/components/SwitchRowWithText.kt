package com.keremmuhcu.flashcardsland.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun SwitchRowWithText(
    text: String,
    enabled: Boolean = true,
    switchState: Boolean,
    switchStateChanged: () -> Unit
) {
    val color = if (enabled) MaterialTheme.colorScheme.onSurface else TextFieldDefaults.colors().disabledTextColor
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, fontFamily = openSansFontFamily, fontSize = 18.sp, color = color)
        Switch(checked = switchState, onCheckedChange = { switchStateChanged() }, enabled = enabled)
    }
}