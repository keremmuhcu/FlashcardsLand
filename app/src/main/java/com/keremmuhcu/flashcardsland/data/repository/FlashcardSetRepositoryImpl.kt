package com.keremmuhcu.flashcardsland.data.repository

import com.keremmuhcu.flashcardsland.data.local.FlashcardSetDao
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import kotlinx.coroutines.flow.Flow

class FlashcardSetRepositoryImpl(
    private val flashcardSetDao: FlashcardSetDao
): FlashcardSetRepository {
    override fun getAllFlashcardSetsWithCards(): Flow<List<FlashcardSetWithCards>> {
        return flashcardSetDao.getAllFlashcardSetsWithCards()
    }

    override suspend fun upsertFlashcardSet(flashcardSet: FlashcardSet) {
        flashcardSetDao.upsertFlashcardSet(flashcardSet)
    }

    override suspend fun deleteFlashcardSet(flashcardSet: FlashcardSet) {
        flashcardSetDao.deleteFlashcardSet(flashcardSet)
    }
}