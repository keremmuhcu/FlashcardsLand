package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.Flashcard

sealed class FlashcardsEvent {
    data class ChangeFlashcardHardness(val flashcard: Flashcard): FlashcardsEvent()
    data class OnSegmentedButtonClicked(val index: Int): FlashcardsEvent()
    data class OnSegmentedButtonClickedHard(val index: Int): FlashcardsEvent()
    data class OnTabSelected(val index: Int): FlashcardsEvent()
    data class OnSearchBarTfChange(val search: TextFieldValue): FlashcardsEvent()
    data class OnSearchIconClicked(val active: Boolean): FlashcardsEvent()
    data class OnDeleteItemClicked(val cardId: Int): FlashcardsEvent()

    // filter
    data object OnConfirmFiltersButtonClicked: FlashcardsEvent()
    data object OnDismissRequestClicked: FlashcardsEvent()
    data class OnDropdownItemClicked(val item: String): FlashcardsEvent()
    data object OnShowDateSwitchClicked: FlashcardsEvent()
    data object OnShowOneSideSwitchClicked: FlashcardsEvent()
    data class OnRadioButtonClicked(val isTermClicked: Boolean): FlashcardsEvent()
    data object OnCanCardFlipCheckBoxClicked: FlashcardsEvent()
}

/*
confirmButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    dropdownItemClicked:(String) -> Unit,
    showDateSwitchChecked: () -> Unit,
    showOneSideSwitchChecked: () -> Unit,
    radioButtonClicked: (Boolean) -> Unit,
    canCardFlipCheckBoxClicked: () -> Unit*/