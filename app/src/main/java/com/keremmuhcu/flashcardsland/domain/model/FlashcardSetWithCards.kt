package com.keremmuhcu.flashcardsland.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class FlashcardSetWithCards(
    @Embedded val flashcardSet: FlashcardSet,
    @Relation(
        parentColumn = "setId",
        entityColumn = "setId"
    )
    val cards: List<Flashcard>
)
