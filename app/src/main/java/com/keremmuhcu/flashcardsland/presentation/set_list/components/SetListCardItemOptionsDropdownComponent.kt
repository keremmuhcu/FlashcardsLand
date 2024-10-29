package com.keremmuhcu.flashcardsland.presentation.set_list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@Composable
fun SetListCardItemOptionsDropdownComponent(
    isExpanded: Boolean,
    onDismissAction:() -> Unit,
    onEditItemClicked:() -> Unit,
    onDeleteItemClicked:() -> Unit
) {
    DropdownMenu(
        expanded = isExpanded, onDismissRequest = { onDismissAction() },
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Düzenle", fontFamily = gintoFontFamily)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
            },
            onClick = { onEditItemClicked() },

        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(text = "Sil", fontFamily = gintoFontFamily)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = MaterialTheme.colorScheme.errorContainer)
            },
            onClick = { onDeleteItemClicked() }
        )
    }
}