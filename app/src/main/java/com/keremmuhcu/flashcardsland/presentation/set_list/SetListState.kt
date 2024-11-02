package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards

data class SetListState(
    val setTitleTextField: TextFieldValue = TextFieldValue(""),
    val flashcardSets: List<FlashcardSetWithCards> = emptyList(),
    val isLoading: Boolean = true,
    val selectedSet: FlashcardSetWithCards? = null
)
