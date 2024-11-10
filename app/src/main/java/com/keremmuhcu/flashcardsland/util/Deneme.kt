package com.keremmuhcu.flashcardsland.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.domain.model.ListSortType
import com.keremmuhcu.flashcardsland.presentation.components.CustomExposedDropdownMenu
import com.keremmuhcu.flashcardsland.presentation.components.SwitchRowWithText
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Deneme() {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.cards_settings),
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            //verticalArrangement = Arrangement.Center
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showDialog = true }) {
                Text(text = "Show Dialog")
            }
        }
        if (showDialog) {
            DialogSection(onDismissRequest = {
                showDialog = false
            })
        }
    }

}

@Composable
fun DialogSection(
    onDismissRequest: () -> Unit
) {
    var alertControl by rememberSaveable { mutableStateOf(false) }
    var hardControl by rememberSaveable { mutableStateOf(false) }
    AlertDialog(
        icon = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onDismissRequest() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "")
                }

                IconButton(onClick = { alertControl = true }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.cards_settings),
                        contentDescription = ""
                    )
                }
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Sadece zorlar", fontFamily = openSansFontFamily)
                Switch(checked = hardControl, onCheckedChange = { hardControl = it })
            }
        },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Image(
                        modifier = Modifier.size(75.dp),
                        painter = painterResource(id = R.drawable.basic_study),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Basit Çalışma",
                        fontFamily = openSansFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Image(
                        modifier = Modifier.size(75.dp),
                        painter = painterResource(id = R.drawable.multiple_study),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Çoktan Seçmeli",
                        fontFamily = openSansFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = { /*TODO*/ },
        shape = RectangleShape
    )

    StudyFilterDialog(
        isDialogOpen = alertControl,
        listSortType = ListSortType.ALPHABETICAL_ASCENDING,
        showOneSide = false,
        confirmButtonClicked = { /*TODO*/ },
        onDismissRequest = { alertControl = false },
        dropdownItemClicked = { alertControl = false },
        showOneSideSwitchChecked = { /*TODO*/ },
    )
}


@PreviewLightDark
@Composable
private fun DePrev() {
    FlashcardsLandTheme {
        Deneme()
    }
}

@Composable
fun StudyFilterDialog(
    isDialogOpen: Boolean,
    listSortType: String,
    showOneSide: Boolean,
    confirmButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    dropdownItemClicked: (String) -> Unit,
    showOneSideSwitchChecked: () -> Unit,
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = confirmButtonClicked) {
                    Text(text = "Onayla", fontFamily = openSansFontFamily)
                }
            },
            text = {
                DialogContent(
                    listSortType = listSortType,
                    showOneSide = showOneSide,
                    dropdownItemClicked = dropdownItemClicked,
                    showOneSideSwitchChecked = showOneSideSwitchChecked,
                )
            },
            shape = RectangleShape
        )
    }
}


@Composable
private fun DialogContent(
    listSortType: String,
    showOneSide: Boolean,
    dropdownItemClicked: (String) -> Unit,
    showOneSideSwitchChecked: () -> Unit,
) {
    val filterTitles = listOf(
        ListSortType.ALPHABETICAL_ASCENDING,
        ListSortType.ALPHABETICAL_DESCENDING,
        ListSortType.DATE_ASCENDING,
        ListSortType.DATE_DESCENDING,
        ListSortType.RANDOM
    )

    val tourCardCounts = listOf(
        "5",
        "10",
        "15",
        "25",
        "50"
    )
    var isExposedMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var isExposedMenuExpanded1 by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        CustomExposedDropdownMenu(
            label = "Kart sırası",
            listSortType = listSortType,
            filterTitles = filterTitles,
            isExposedMenuExpanded = isExposedMenuExpanded,
            onExpandedChange = { isExposedMenuExpanded = !isExposedMenuExpanded },
            onDropdownItemClick = dropdownItemClicked
        )
        CustomExposedDropdownMenu(
            label = "Bir turdaki kart sayısı",
            listSortType = "10",
            filterTitles = tourCardCounts,
            isExposedMenuExpanded = isExposedMenuExpanded1,
            onExpandedChange = { isExposedMenuExpanded1 = !isExposedMenuExpanded1 },
            onDropdownItemClick = dropdownItemClicked
        )
        SwitchRowWithText(text = "Tanımları çalış", switchState = showOneSide, switchStateChanged = showOneSideSwitchChecked)
    }
}







