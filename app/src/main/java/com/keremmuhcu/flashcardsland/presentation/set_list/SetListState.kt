package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import com.keremmuhcu.flashcardsland.domain.model.Settings

data class SetListState(
    val setTitleTextField: TextFieldValue = TextFieldValue(""),
    val flashcardSets: List<FlashcardSetWithCards> = emptyList(),
    val isLoading: Boolean = true,
    val resetProgress: Boolean = false,
    val selectedSet: FlashcardSetWithCards? = null,
    // study - filters
    val settings: Settings = Settings(),
    val studySortType: String = "",
    val cardCountOneRound: String = "",
    val workDefinitions: Boolean = false,
    val workOnlyHard: Boolean = false
)
