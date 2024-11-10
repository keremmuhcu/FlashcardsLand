package com.keremmuhcu.flashcardsland.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardSetDao {
    @Transaction
    @Query("SELECT * FROM flashcard_set ORDER BY updatedAt DESC")
    fun getAllFlashcardSetsWithCards(): Flow<List<FlashcardSetWithCards>>

    @Upsert
    suspend fun upsertFlashcardSet(flashcardSet: FlashcardSet)

    @Delete
    suspend fun deleteFlashcardSet(flashcardSet: FlashcardSet)

    @Query("UPDATE flashcard_set SET updatedAt = :updatedAt WHERE setId = :setId")
    suspend fun editUpdatedAt(setId: Int, updatedAt: Long)

}