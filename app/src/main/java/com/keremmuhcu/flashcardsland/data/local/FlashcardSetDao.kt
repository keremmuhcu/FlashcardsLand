package com.keremmuhcu.flashcardsland.data.local

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import androidx.room.Dao
import androidx.room.Transaction
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardSetDao {
    @Transaction
    @Query("SELECT * FROM flashcard_set")
    fun getAllFlashcardSetsWithCards(): Flow<List<FlashcardSetWithCards>>

    @Upsert
    suspend fun upsertFlashcardSet(flashcardSet: FlashcardSet)

    @Delete
    suspend fun deleteFlashcardSet(flashcardSet: FlashcardSet)
}