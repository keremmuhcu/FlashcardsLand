package com.keremmuhcu.flashcardsland.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcard",
    foreignKeys = [ForeignKey(
        entity = FlashcardSet::class,
        parentColumns = ["setId"],
        childColumns = ["setId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["setId"])]
)
data class Flashcard(
    val setId: Int,
    @PrimaryKey(autoGenerate = true) val cardId: Int? = null,
    val term: String,
    val definition: String,
    val examples: List<String> = emptyList(),
    val isStudied: Boolean,
    val isHard: Boolean,
    val isHardStudied: Boolean,
    val createdDate: Long = System.currentTimeMillis()
)

