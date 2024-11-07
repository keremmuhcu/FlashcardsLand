package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.Settings

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
    //filters
    val settings: Settings = Settings(),
    val selectedSortType: String = "",
    val showDateSwitch: Boolean = false,
    val showOneSideSwitch: Boolean = false,
    val showOnlyTermRadioButton: Boolean = false,
    val canCardFlip: Boolean = false
)
