package com.keremmuhcu.flashcardsland.presentation.study.basic

import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.Settings

data class BasicStudyState(
    val flashcards: List<Flashcard> = emptyList(),
    val settings: Settings = Settings(),
    val currentCardIndex: Int = 0,
    val termSide: String = "",
    val definitionSide: String = "",
    val corrects: Int = 0,
    val wrongs: Int = 0,
    val remainingCards: Int = 0,
    val currentCardIsHard: Boolean = false,
    val isLoading:Boolean = true,
    val isFinish: Boolean = false
)
