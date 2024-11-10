package com.keremmuhcu.flashcardsland.presentation.set_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.domain.model.ListSortType
import com.keremmuhcu.flashcardsland.presentation.components.CustomExposedDropdownMenu
import com.keremmuhcu.flashcardsland.presentation.components.SwitchRowWithText
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun StudyFiltersDialog(
    isOpen: Boolean,
    studySortType: String,
    studySortTypeChanged: (String) -> Unit,
    cardCountOneRound: String,
    cardCountOneRoundChanged: (String) -> Unit,
    workDefinitions: Boolean,
    workDefinitionsSwitchClicked: () -> Unit,
    confirmButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (isOpen) {
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
                    studySortType = studySortType,
                    studySortTypeChanged = studySortTypeChanged,
                    cardCountOneRound = cardCountOneRound,
                    cardCountOneRoundChanged = cardCountOneRoundChanged,
                    workDefinitions = workDefinitions,
                    workDefinitionsSwitchClicked = workDefinitionsSwitchClicked
                )
            },
            shape = RectangleShape
        )
    }
}

@Composable
private fun DialogContent(
    studySortType: String,
    studySortTypeChanged: (String) -> Unit,
    cardCountOneRound: String,
    cardCountOneRoundChanged: (String) -> Unit,
    workDefinitions: Boolean,
    workDefinitionsSwitchClicked: () -> Unit,
) {
    val filterTitles = listOf(
        ListSortType.ALPHABETICAL_ASCENDING,
        ListSortType.ALPHABETICAL_DESCENDING,
        ListSortType.DATE_ASCENDING,
        ListSortType.DATE_DESCENDING,
        ListSortType.RANDOM
    )

    val tourCardCounts = listOf("5", "10", "15", "25", "50")
    var isSortExposedMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var isCardCountExposedMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        CustomExposedDropdownMenu(
            label = "Kart sırası",
            listSortType = studySortType,
            filterTitles = filterTitles,
            isExposedMenuExpanded = isSortExposedMenuExpanded,
            onExpandedChange = { isSortExposedMenuExpanded = !isSortExposedMenuExpanded },
            onDropdownItemClick = studySortTypeChanged
        )

        CustomExposedDropdownMenu(
            label = "Bir turdaki kart sayısı",
            listSortType = cardCountOneRound,
            filterTitles = tourCardCounts,
            isExposedMenuExpanded = isCardCountExposedMenuExpanded,
            onExpandedChange = { isCardCountExposedMenuExpanded = !isCardCountExposedMenuExpanded },
            onDropdownItemClick = cardCountOneRoundChanged
        )
        SwitchRowWithText(text = "Tanımları çalış", switchState = workDefinitions, switchStateChanged = workDefinitionsSwitchClicked)

    }
}