package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun SwitchesComponent(
    switchState: Boolean,
    hardSwitchState: Boolean,
    onSwitchChange: () -> Unit,
    onHardSwitchChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Zor",
            fontFamily = openSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Switch(
            checked = hardSwitchState,
            onCheckedChange = { onHardSwitchChange() },
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Örnekler",
            fontFamily = openSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Switch(
            checked = switchState,
            onCheckedChange = { onSwitchChange() },
        )
    }
}