package com.keremmuhcu.flashcardsland.presentation.study.multiple_answers

import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.Settings

data class MultipleAnswersStudyState(
    val isLoading: Boolean = true,
    val flashcards: List<Flashcard> = emptyList(),
    val currentCardIsHard: Boolean = false,
    val question: String = "",
    val answers: List<String> = emptyList(),
    val options: List<String> = emptyList(),
    val currentCardIndex: Int = 0,
    val corrects: Int = 0,
    val wrongs: Int = 0,
    val remainingCards: Int = 0,
    val isFinish: Boolean = false,
    val settings: Settings = Settings()
)
