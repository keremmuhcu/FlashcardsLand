package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components.FlashcardTermDefinitionComponent
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components.SwitchesComponent
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components.flashcardExamplesList
import com.keremmuhcu.flashcardsland.presentation.components.CustomAlertDialog
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddOrEditFlashcardScreen(
    addOrEditFlashcardViewModel: AddOrEditFlashcardViewModel = koinViewModel<AddOrEditFlashcardViewModel>(),
    //onEvent: (AddOrEditFlashcardEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by addOrEditFlashcardViewModel.state.collectAsStateWithLifecycle()

    val buttonsActivityControl by derivedStateOf {
        state.termTf.text.isNotBlank() && state.definitionTf.text.isNotBlank()
                && (!state.isExampleSwitchChecked || state.examplesTfList.all {
            it.text.isNotBlank()
        })
    }

    val isThereAnyChanges by derivedStateOf {
        state.selectedFlashcard?.let {
            it.term != state.termTf.text
                    || it.definition != state.definitionTf.text
                    || it.examples != state.examplesTfList.map {tf-> tf.text }
                    || it.isHard != state.isHardSwitchChecked
        }
    }

    Scaffold(
        topBar = {
            AddOrEditFlashcardTopBar(
                buttonsActivityControl = buttonsActivityControl,
                isThereAnyChanges = isThereAnyChanges,
                onCloseClicked = {
                    onNavigateBack()
                },
                onSaveClicked = {
                    addOrEditFlashcardViewModel.onEvent(AddOrEditFlashcardEvent.OnSaveButtonClicked)
                    onNavigateBack()
                },
                onDeleteClicked = {
                    addOrEditFlashcardViewModel.onEvent(AddOrEditFlashcardEvent.OnDeleteButtonClicked)
                    onNavigateBack()
                },
                showDeleteButton = state.selectedFlashcard != null
            )
        }
    ) { innerPadding ->
        AddOrEditFlashcardScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
            state = state,
            onEvent = addOrEditFlashcardViewModel::onEvent,
            buttonsActivityControl = buttonsActivityControl
        )
    }
}

@Composable
fun AddOrEditFlashcardScreenContent(
    modifier: Modifier = Modifier,
    state: AddOrEditFlashcardState,
    onEvent: (AddOrEditFlashcardEvent) -> Unit,
    buttonsActivityControl: Boolean
) {
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                FlashcardTermDefinitionComponent(
                    modifier = Modifier.fillMaxWidth(),
                    focusTermTextField = state.isSuccessful,
                    termTextField = state.termTf,
                    onTermTextFieldChange = {
                        onEvent(
                            AddOrEditFlashcardEvent.OnTermTextFieldChange(
                                it
                            )
                        )
                    },
                    definitionTextField = state.definitionTf,
                    onDefinitionTextFieldChange = {
                        onEvent(
                            AddOrEditFlashcardEvent.OnDefinitionTextFieldChange(
                                it
                            )
                        )
                    },
                    tfTextStyle = TextStyle(
                        fontFamily = openSansFontFamily,
                        fontSize = 16.sp
                    )
                )

                Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    SwitchesComponent(
                        switchState = state.isExampleSwitchChecked,
                        hardSwitchState = state.isHardSwitchChecked,
                        onSwitchChange = { onEvent(AddOrEditFlashcardEvent.OnExampleSwitchChange) },
                        onHardSwitchChange = { onEvent(AddOrEditFlashcardEvent.OnHardSwitchChange) }
                    )
                }
            }

            if (state.isExampleSwitchChecked) { // exampleSwitchState
                flashcardExamplesList(
                    examplesTextFields = state.examplesTfList,
                    listSize = state.examplesTfList.size,
                    onExampleTextFieldChange = { index, text ->
                        onEvent(AddOrEditFlashcardEvent.OnExampleTextFieldChange(index, text))
                    },
                    removeExampleIconClicked = { index ->
                        onEvent(AddOrEditFlashcardEvent.OnDeleteExampleIconClicked(index))
                    },
                    addExampleIconClicked = { onEvent(AddOrEditFlashcardEvent.OnAddExampleIconClicked) }
                )
            }
        }


        if (state.selectedFlashcard == null) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                onClick = { onEvent(AddOrEditFlashcardEvent.OnSaveAndNextButtonClicked) },
                enabled = buttonsActivityControl
            ) {
                Text(text = "YENİ KART EKLE", fontFamily = openSansFontFamily, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrEditFlashcardTopBar(
    buttonsActivityControl: Boolean,
    isThereAnyChanges: Boolean?,
    onCloseClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    showDeleteButton: Boolean
) {
    val alertControl = remember { mutableStateMapOf("delete" to false, "back" to false) }
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                if (isThereAnyChanges?.let { it && buttonsActivityControl } ?: buttonsActivityControl) {
                    alertControl["back"] = true
                } else {
                    onCloseClicked()
                }

            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        },
        actions = {
            if (showDeleteButton) {
                TextButton(onClick = { alertControl["delete"] = true }) {
                    Text(
                        text = "Sil",
                        fontFamily = openSansFontFamily,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            TextButton(
                onClick = { onSaveClicked() },
                enabled = buttonsActivityControl
            ) {
                Text(text = "Kaydet", fontFamily = openSansFontFamily, fontSize = 16.sp)
            }

        }
    )

    CustomAlertDialog(
        title = "Kaydedilmemiş değişiklikler",
        text = "Şimdi çıkarsanız kart" + if(isThereAnyChanges!=null) " düzenlenmeyecek." else " eklenmeyecek.",
        isOpen = alertControl["back"]!!,
        onConfirm = {
            alertControl["back"] = false
            onCloseClicked()
        },
        onCancel = { alertControl["back"] = false }
    )

    CustomAlertDialog(
        title = "Kart silinecek",
        text = "Bu işlem geri alınamaz",
        isOpen = alertControl["delete"]!!,
        onConfirm = {
            alertControl["delete"] = false
            onDeleteClicked()
        },
        onCancel = { alertControl["delete"] = false }
    )
}
