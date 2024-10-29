package com.keremmuhcu.flashcardsland.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcard WHERE setId = :setId")
    fun getFlashcardsBySetId(setId: Int): Flow<List<Flashcard>>

    @Upsert
    suspend fun upsertFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)
}
