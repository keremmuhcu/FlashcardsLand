package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import androidx.compose.ui.text.input.TextFieldValue

sealed class AddOrEditFlashcardEvent {
    data class OnTermTextFieldChange(val term: TextFieldValue) : AddOrEditFlashcardEvent()
    data class OnDefinitionTextFieldChange(val definition: TextFieldValue) : AddOrEditFlashcardEvent()
    data class OnExampleTextFieldChange(val index: Int, val example: TextFieldValue) : AddOrEditFlashcardEvent()
    data class OnDeleteExampleIconClicked(val index: Int) : AddOrEditFlashcardEvent()
    data object OnAddExampleIconClicked : AddOrEditFlashcardEvent()
    data object OnHardSwitchChange : AddOrEditFlashcardEvent()
    data object OnExampleSwitchChange : AddOrEditFlashcardEvent()
    data object OnSaveButtonClicked : AddOrEditFlashcardEvent()
    data object OnDeleteButtonClicked: AddOrEditFlashcardEvent()
    data object OnSaveAndNextButtonClicked: AddOrEditFlashcardEvent()
}