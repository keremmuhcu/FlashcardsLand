package com.keremmuhcu.flashcardsland.domain.repository

import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.Settings
import com.keremmuhcu.flashcardsland.domain.model.StudyFlashcardsOrderType
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getFlashcardsBySetId(setId: Int): Flow<List<Flashcard>>
    suspend fun upsertFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)
    fun getHardFlashcards(): Flow<List<Flashcard>>
    suspend fun getFlashcardById(id: Int): Flashcard
    suspend fun getUnstudiedFlashcardsCount(setId: Int): Int
    fun getFlashcardListFilters(): Flow<Settings>
    suspend fun updateSettings(settings: Settings)
    suspend fun deleteFlashcardById(cardId: Int)
    suspend fun getFalseAnswers(setId: Int, cardId: Int): List<Flashcard>
    suspend fun getUnstudiedFlashcardsForStudy(
        setId: Int,
        studyType: String,
        isHard: Boolean,
        sortOrder: StudyFlashcardsOrderType,
        limit: Int
    ): List<Flashcard>
}