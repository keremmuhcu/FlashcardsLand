package com.keremmuhcu.flashcardsland.presentation.study.basic.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun ExpandableExamplesTitleText(
    isExamplesExpanded: Boolean,
    onTitleClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onTitleClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Örnekler",
            fontFamily = openSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Icon(imageVector = if (isExamplesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = "")
    }
}