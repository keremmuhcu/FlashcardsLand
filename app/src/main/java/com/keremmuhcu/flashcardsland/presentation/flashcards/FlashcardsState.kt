package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.Flashcard

data class FlashcardsState(
    val flashcards: List<Flashcard> = emptyList(),
    val selectedSetTitle: String = "",
    val isLoading: Boolean = true,
    val selectedFlashcard: Flashcard? = null,
    val selectedSegmentButtonIndex: Int = 0,
    val selectedSegmentButtonIndexHard: Int = 0,
    val selectedTabIndex: Int = 0,
    val searchBarTextTf: TextFieldValue = TextFieldValue(""),
    val searchBarActive: Boolean = false,
)
