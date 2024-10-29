package com.keremmuhcu.flashcardsland.data.repository

import com.keremmuhcu.flashcardsland.data.local.FlashcardDao
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao
): FlashcardRepository {
    override fun getFlashcardsBySetId(setId: Int): Flow<List<Flashcard>> {
        return flashcardDao.getFlashcardsBySetId(setId)
    }

    override suspend fun upsertFlashcard(flashcard: Flashcard) {
        flashcardDao.upsertFlashcard(flashcard)
    }

    override suspend fun deleteFlashcard(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }
}