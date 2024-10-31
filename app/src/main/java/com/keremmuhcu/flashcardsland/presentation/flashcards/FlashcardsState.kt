package com.keremmuhcu.flashcardsland.presentation.flashcards

import com.keremmuhcu.flashcardsland.domain.model.Flashcard

data class FlashcardsState(
    val flashcards: List<Flashcard> = emptyList(),
    val selectedSetTitle: String = "",
    val selectedSetId: Int = -1,
    val isLoading: Boolean = true
)
