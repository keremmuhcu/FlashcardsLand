package com.keremmuhcu.flashcardsland.domain.repository

import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import kotlinx.coroutines.flow.Flow

interface FlashcardSetRepository {
    fun getAllFlashcardSetsWithCards(): Flow<List<FlashcardSetWithCards>>
    suspend fun upsertFlashcardSet(flashcardSet: FlashcardSet)
    suspend fun deleteFlashcardSet(flashcardSet: FlashcardSet)
}