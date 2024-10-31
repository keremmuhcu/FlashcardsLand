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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components.FlashcardTermDefinitionComponent
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components.SwitchesComponent
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components.flashcardExamplesList
import com.keremmuhcu.flashcardsland.presentation.components.CustomAlertDialog
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddOrEditFlashcardScreen(
    state: State<AddOrEditFlashcardState>,
    onEvent: (AddOrEditFlashcardEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    val buttonsActivityControl by derivedStateOf {
        state.value.termTf.isNotBlank() && state.value.definitionTf.isNotBlank()
                && (!state.value.isExampleSwitchChecked || state.value.examplesTfList.all {
            it.isNotBlank()
        })
    }

    Scaffold(
        topBar = {
            AddOrEditFlashcardTopBar(
                buttonsActivityControl = buttonsActivityControl,
                onCloseClicked = {
                    onNavigateBack()
                },
                onSaveClicked = {
                    onEvent(AddOrEditFlashcardEvent.OnSaveButtonClicked)
                    onNavigateBack()
                },
                onDeleteClicked = {
                    onEvent(AddOrEditFlashcardEvent.OnDeleteButtonClicked)
                    onNavigateBack()
                },
                showDeleteButton = state.value.selectedFlashcard != null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    FlashcardTermDefinitionComponent(
                        modifier = Modifier.fillMaxWidth(),
                        focusTermTextField = state.value.isSuccessful,
                        termTextField = state.value.termTf,
                        onTermTextFieldChange = {
                            onEvent(
                                AddOrEditFlashcardEvent.OnTermTextFieldChange(
                                    it
                                )
                            )
                        },
                        definitionTextField = state.value.definitionTf,
                        onDefinitionTextFieldChange = {
                            onEvent(
                                AddOrEditFlashcardEvent.OnDefinitionTextFieldChange(
                                    it
                                )
                            )
                        },
                        tfTextStyle = TextStyle(
                            fontFamily = gintoFontFamily,
                            fontSize = 16.sp
                        )
                    )

                    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                        SwitchesComponent(
                            switchState = state.value.isExampleSwitchChecked,
                            hardSwitchState = state.value.isHardSwitchChecked,
                            onSwitchChange = { onEvent(AddOrEditFlashcardEvent.OnExampleSwitchChange) },
                            onHardSwitchChange = { onEvent(AddOrEditFlashcardEvent.OnHardSwitchChange) }
                        )
                    }
                }

                if (state.value.isExampleSwitchChecked) { // exampleSwitchState
                    flashcardExamplesList(
                        examplesTextFields = state.value.examplesTfList,
                        listSize = state.value.examplesTfList.size,
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

            if (state.value.selectedFlashcard == null) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = { onEvent(AddOrEditFlashcardEvent.OnSaveAndNextButtonClicked) },
                    enabled = buttonsActivityControl
                ) {
                    Text(text = "YENİ KART EKLE", fontFamily = gintoFontFamily, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrEditFlashcardTopBar(
    buttonsActivityControl: Boolean,
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
                if (buttonsActivityControl) {
                    alertControl["back"] = true
                }else{onCloseClicked()}

            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        },
        actions = {
            if (showDeleteButton) {
                TextButton(onClick = { alertControl["delete"] = true }) {
                    Text(
                        text = "Sil",
                        fontFamily = gintoFontFamily,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            TextButton(
                onClick = { onSaveClicked() },
                enabled = buttonsActivityControl
            ) {
                Text(text = "Kaydet", fontFamily = gintoFontFamily, fontSize = 16.sp)
            }

        }
    )

    CustomAlertDialog(
        title = "Kaydedilmemiş değişiklikler",
        text = "Şimdi çıkarsanız kart eklenmeyecek.",
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
