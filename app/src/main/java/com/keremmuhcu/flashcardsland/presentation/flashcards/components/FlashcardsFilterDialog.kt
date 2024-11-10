package com.keremmuhcu.flashcardsland.presentation.flashcards.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.domain.model.ListSortType
import com.keremmuhcu.flashcardsland.presentation.components.CustomExposedDropdownMenu
import com.keremmuhcu.flashcardsland.presentation.components.SwitchRowWithText
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun FlashcardsFilterDialog(
    isDialogOpen: Boolean,
    listSortType: String,
    showDate: Boolean,
    showOneSide: Boolean,
    showOnlyTerm: Boolean,
    cardCanFlip: Boolean,
    confirmButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    dropdownItemClicked: (String) -> Unit,
    showDateSwitchChecked: () -> Unit,
    showOneSideSwitchChecked: () -> Unit,
    radioButtonClicked: (Boolean) -> Unit,
    canCardFlipCheckBoxClicked: () -> Unit
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = {},
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "İptal", fontFamily = openSansFontFamily)
                }
            },
            confirmButton = {
                TextButton(onClick = confirmButtonClicked) {
                    Text(text = "Onayla", fontFamily = openSansFontFamily)
                }
            },
            text = {
                DialogContent(
                    listSortType = listSortType,
                    showDate = showDate,
                    showOneSide = showOneSide,
                    showOnlyTerm = showOnlyTerm,
                    cardCanFlip = cardCanFlip,
                    dropdownItemClicked = dropdownItemClicked,
                    showDateSwitchChecked = showDateSwitchChecked,
                    showOneSideSwitchChecked = showOneSideSwitchChecked,
                    radioButtonClicked = radioButtonClicked,
                    canCardFlipCheckBoxClicked = canCardFlipCheckBoxClicked
                )
            },
            shape = RectangleShape
        )
    }
}


@Composable
private fun DialogContent(
    listSortType: String,
    showDate: Boolean,
    showOneSide: Boolean,
    showOnlyTerm: Boolean,
    cardCanFlip: Boolean,
    dropdownItemClicked: (String) -> Unit,
    showDateSwitchChecked: () -> Unit,
    showOneSideSwitchChecked: () -> Unit,
    radioButtonClicked: (Boolean) -> Unit,
    canCardFlipCheckBoxClicked: () -> Unit
) {
    val filterTitles = listOf(
        ListSortType.ALPHABETICAL_ASCENDING,
        ListSortType.ALPHABETICAL_DESCENDING,
        ListSortType.DATE_ASCENDING,
        ListSortType.DATE_DESCENDING
    )
    var isExposedMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        CustomExposedDropdownMenu(
            label = "Kartları sırala",
            listSortType = listSortType,
            filterTitles = filterTitles,
            isExposedMenuExpanded = isExposedMenuExpanded,
            onExpandedChange = { isExposedMenuExpanded = !isExposedMenuExpanded },
            onDropdownItemClick = dropdownItemClicked
        )
        ShowDateSwitch(showDate = showDate, listSortType = listSortType, showDateSwitchChecked = showDateSwitchChecked)
        SwitchRowWithText(text = "Tek taraf göster", switchState = showOneSide, switchStateChanged = showOneSideSwitchChecked)
        RadioButtonOptions(showOneSide = showOneSide, showOnlyTerm = showOnlyTerm, radioButtonClicked = radioButtonClicked)
        CardFlipOption(showOneSide = showOneSide, cardCanFlip = cardCanFlip, canCardFlipCheckBoxClicked = canCardFlipCheckBoxClicked)
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortTypeDropdown(
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
}*/

@Composable
private fun ShowDateSwitch(
    showDate: Boolean,
    listSortType: String,
    showDateSwitchChecked: () -> Unit
) {
    val filterTitles = listOf(ListSortType.DATE_ASCENDING, ListSortType.DATE_DESCENDING)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Tarihi göster",
            fontFamily = openSansFontFamily,
            fontSize = 18.sp,
            color = if (listSortType in filterTitles) MaterialTheme.colorScheme.onSurface else OutlinedTextFieldDefaults.colors().disabledTextColor
        )
        Switch(checked = showDate, onCheckedChange = { showDateSwitchChecked() }, enabled = listSortType in filterTitles)
    }
}
/*
@Composable
private fun ShowOneSideSwitch(
    showOneSide: Boolean,
    showOneSideSwitchChecked: () -> Unit
) {
    val textColor = if (showOneSide) MaterialTheme.colorScheme.onSurface else OutlinedTextFieldDefaults.colors().disabledTextColor
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Tek taraf göster", fontFamily = openSansFontFamily, fontSize = 18.sp, color = textColor)
        Switch(checked = showOneSide, onCheckedChange = { showOneSideSwitchChecked() })
    }
}
*/
@Composable
private fun RadioButtonOptions(
    showOneSide: Boolean,
    showOnlyTerm: Boolean,
    radioButtonClicked: (Boolean) -> Unit
) {
    val color = if (showOneSide) MaterialTheme.colorScheme.onSurface else RadioButtonDefaults.colors().disabledUnselectedColor
    RowOption("Terim", selected = showOnlyTerm, enabled = showOneSide, color = color) {
        radioButtonClicked(true)
    }
    RowOption("Tanım", selected = !showOnlyTerm, enabled = showOneSide, color = color) {
        radioButtonClicked(false)
    }
}

@Composable
private fun CardFlipOption(
    showOneSide: Boolean,
    cardCanFlip: Boolean,
    canCardFlipCheckBoxClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = showOneSide) {
                canCardFlipCheckBoxClicked()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = cardCanFlip, onCheckedChange = { canCardFlipCheckBoxClicked() }, enabled = showOneSide)
        Text(text = "Kart döndürme", fontFamily = openSansFontFamily, color = if (showOneSide) MaterialTheme.colorScheme.onSurface else CheckboxDefaults.colors().disabledCheckedBoxColor)
    }
}

@Composable
private fun RowOption(text: String, selected: Boolean, enabled: Boolean, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick, enabled = enabled)
        Text(text = text, fontFamily = openSansFontFamily, color = color)
    }
}
