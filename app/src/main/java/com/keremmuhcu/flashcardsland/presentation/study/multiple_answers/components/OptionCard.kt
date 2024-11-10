package com.keremmuhcu.flashcardsland.presentation.study.multiple_answers.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.presentation.study.multiple_answers.getContainerColor
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun OptionCard(
    answer: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isSelectedWrong: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f), shape = CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = getContainerColor(isSelected, isCorrect, isSelectedWrong),
            disabledContainerColor = getContainerColor(isSelected, isCorrect, isSelectedWrong),
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick,
        enabled = !isSelected
    ) {
        Text(
            modifier = Modifier.padding(all = 12.dp),
            text = answer,
            fontFamily = openSansFontFamily,
            fontSize = 16.sp
        )
    }
}