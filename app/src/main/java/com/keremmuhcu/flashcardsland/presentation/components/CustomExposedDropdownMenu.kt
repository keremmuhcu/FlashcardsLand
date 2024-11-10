package com.keremmuhcu.flashcardsland.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomExposedDropdownMenu(
    label: String,
    listSortType: String,
    filterTitles: List<String>,
    isExposedMenuExpanded: Boolean,
    onExpandedChange: () -> Unit,
    onDropdownItemClick: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = isExposedMenuExpanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = listSortType,
            label = { Text(text = label, fontFamily = openSansFontFamily) },
            onValueChange = {},
            textStyle = TextStyle(fontFamily = openSansFontFamily),
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExposedMenuExpanded) }
        )
        ExposedDropdownMenu(expanded = isExposedMenuExpanded, onDismissRequest = onExpandedChange) {
            filterTitles.forEach { title ->
                DropdownMenuItem(
                    text = { Text(text = title, fontFamily = openSansFontFamily) },
                    onClick = {
                        onDropdownItemClick(title)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}