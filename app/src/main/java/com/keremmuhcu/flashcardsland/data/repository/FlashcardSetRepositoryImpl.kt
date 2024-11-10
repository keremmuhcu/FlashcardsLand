package com.keremmuhcu.flashcardsland.data.repository

import com.keremmuhcu.flashcardsland.data.local.FlashcardDao
import com.keremmuhcu.flashcardsland.data.local.FlashcardSetDao
import com.keremmuhcu.flashcardsland.data.local.SettingsDao
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import com.keremmuhcu.flashcardsland.domain.model.Settings
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import kotlinx.coroutines.flow.Flow

class FlashcardSetRepositoryImpl(
    private val flashcardSetDao: FlashcardSetDao,
    private val flashcardDao: FlashcardDao,
    private val settingsDao: SettingsDao
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

    override suspend fun editUpdatedAt(setId: Int, updatedAt: Long) {
        flashcardSetDao.editUpdatedAt(setId, updatedAt)
    }

    override suspend fun getDarkMode(): Boolean {
        return settingsDao.getDarkMode()
    }

    override suspend fun updateDarkMode(isDarkMode: Boolean) {
        settingsDao.updateDarkMode(isDarkMode)
    }

    override fun getFlashcardListFilters(): Flow<Settings> {
        return settingsDao.getSettings()
    }

    override suspend fun updateWorkHard(isWorkHard: Boolean) {
        settingsDao.updateWorkHard(isWorkHard)
    }

    override suspend fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

    override suspend fun resetHardProgress(setId: Int) {
        flashcardDao.resetHardProgress(setId)
    }

    override suspend fun resetNormalProgress(setId: Int) {
        flashcardDao.resetNormalProgress(setId)
    }
}