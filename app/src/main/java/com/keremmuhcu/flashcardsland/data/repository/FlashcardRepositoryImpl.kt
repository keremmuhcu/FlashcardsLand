package com.keremmuhcu.flashcardsland.data.repository

import com.keremmuhcu.flashcardsland.data.local.FlashcardDao
import com.keremmuhcu.flashcardsland.data.local.FlashcardSetDao
import com.keremmuhcu.flashcardsland.data.local.SettingsDao
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.Settings
import com.keremmuhcu.flashcardsland.domain.model.StudyFlashcardsOrderType
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao,
    private val flashcardSetDao: FlashcardSetDao,
    private val settingsDao: SettingsDao
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

    override suspend fun getUnstudiedFlashcardsCount(setId: Int): Int {
        return flashcardDao.getUnstudiedFlashcardsCount(setId)
    }

    override suspend fun getUnstudiedHardFlashcardsCount(setId: Int): Int {
        return flashcardDao.getUnstudiedHardFlashcardsCount(setId)
    }

    override fun getFlashcardListFilters(): Flow<Settings> {
        return settingsDao.getSettings()
    }

    override suspend fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

    override suspend fun deleteFlashcardById(cardId: Int) {
        flashcardDao.deleteFlashcardById(cardId)
    }

    override suspend fun getFalseAnswers(setId: Int, cardId: Int): List<Flashcard> {
        return flashcardDao.getFalseAnswers(setId, cardId)
    }

    override suspend fun getUnstudiedFlashcardsForStudy(
        setId: Int,
        studyType: String,
        isHard: Boolean,
        sortOrder: StudyFlashcardsOrderType,
        limit: Int
    ): List<Flashcard> {
        return flashcardDao.getUnstudiedFlashcardsForStudy(
            setId = setId,
            studyType = studyType,
            isHard = isHard,
            sortOrder = sortOrder.name,
            limit = limit
        )
    }


}