package com.keremmuhcu.flashcardsland.presentation.set_list

import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards

data class SetListState(
    val setTitleTextField: String = "",
    val flashcardSets: List<FlashcardSetWithCards> = emptyList(),
    val isLoading: Boolean = true,
    val selectedSet: FlashcardSetWithCards? = null
)
