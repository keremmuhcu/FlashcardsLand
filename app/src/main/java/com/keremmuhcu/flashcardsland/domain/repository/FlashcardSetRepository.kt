package com.keremmuhcu.flashcardsland.domain.repository

import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import com.keremmuhcu.flashcardsland.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface FlashcardSetRepository {
    fun getAllFlashcardSetsWithCards(): Flow<List<FlashcardSetWithCards>>
    suspend fun upsertFlashcardSet(flashcardSet: FlashcardSet)
    suspend fun deleteFlashcardSet(flashcardSet: FlashcardSet)
    suspend fun editUpdatedAt(setId: Int, updatedAt: Long)
    suspend fun getDarkMode(): Boolean
    suspend fun updateDarkMode(isDarkMode: Boolean)
    fun getFlashcardListFilters(): Flow<Settings>
    suspend fun updateWorkHard(isWorkHard: Boolean)
    suspend fun updateSettings(settings: Settings)
    suspend fun resetHardProgress(setId: Int)
    suspend fun resetNormalProgress(setId: Int)

}