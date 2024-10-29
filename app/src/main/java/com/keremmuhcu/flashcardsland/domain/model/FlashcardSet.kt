package com.keremmuhcu.flashcardsland.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_set")
data class FlashcardSet(
    @PrimaryKey(autoGenerate = true) val setId: Int,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
