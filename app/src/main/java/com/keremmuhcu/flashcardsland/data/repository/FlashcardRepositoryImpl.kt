package com.keremmuhcu.flashcardsland.data.repository

import com.keremmuhcu.flashcardsland.data.local.FlashcardDao
import com.keremmuhcu.flashcardsland.data.local.FlashcardSetDao
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao,
    private val flashcardSetDao: FlashcardSetDao
): FlashcardRepository {
    override fun getFlashcardsBySetId(setId: Int): Flow<List<Flashcard>> {
        return flashcardDao.getFlashcardsBySetId(setId)
    }

    override suspend fun upsertFlashcard(flashcard: Flashcard) {
        flashcardDao.upsertFlashcard(flashcard)
        flashcardSetDao.editUpdatedAt(flashcard.setId, System.currentTimeMillis())
    }

    override suspend fun deleteFlashcard(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }

    override fun getHardFlashcards(): Flow<List<Flashcard>> {
        return flashcardDao.getHardFlashcards()
    }

    override suspend fun getFlashcardById(id: Int): Flashcard {
        return flashcardDao.getFlashcardById(id)
    }

    override suspend fun getRandom10UnstudiedFlashcards(setId: Int): List<Flashcard> {
        return flashcardDao.getRandom10UnstudiedFlashcards(setId = setId)
    }

    override suspend fun getUnstudiedFlashcardsCount(setId: Int): Int {
        return flashcardDao.getUnstudiedFlashcardsCount(setId)
    }
}