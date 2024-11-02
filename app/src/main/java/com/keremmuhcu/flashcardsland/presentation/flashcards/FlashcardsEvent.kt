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
}