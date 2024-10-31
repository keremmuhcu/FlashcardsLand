package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

sealed class AddOrEditFlashcardEvent {
    data class OnTermTextFieldChange(val term: String) : AddOrEditFlashcardEvent()
    data class OnDefinitionTextFieldChange(val definition: String) : AddOrEditFlashcardEvent()
    data class OnExampleTextFieldChange(val index: Int, val example: String) : AddOrEditFlashcardEvent()
    data class OnDeleteExampleIconClicked(val index: Int) : AddOrEditFlashcardEvent()
    data object OnAddExampleIconClicked : AddOrEditFlashcardEvent()
    data object OnHardSwitchChange : AddOrEditFlashcardEvent()
    data object OnExampleSwitchChange : AddOrEditFlashcardEvent()
    data object OnSaveButtonClicked : AddOrEditFlashcardEvent()
    data object OnDeleteButtonClicked: AddOrEditFlashcardEvent()
    data object OnSaveAndNextButtonClicked: AddOrEditFlashcardEvent()
}