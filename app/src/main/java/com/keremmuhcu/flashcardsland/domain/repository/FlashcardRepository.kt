package com.keremmuhcu.flashcardsland.domain.repository

import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getFlashcardsBySetId(setId: Int): Flow<List<Flashcard>>
    suspend fun upsertFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)
    fun getHardFlashcards(): Flow<List<Flashcard>>
    suspend fun getFlashcardById(id: Int): Flashcard
    suspend fun getRandom10UnstudiedFlashcards(setId: Int): List<Flashcard>
    suspend fun getUnstudiedFlashcardsCount(setId: Int): Int
}