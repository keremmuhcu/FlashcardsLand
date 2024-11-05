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

    @Query("SELECT * FROM flashcard WHERE isHard = 1")
    fun getHardFlashcards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcard WHERE cardId = :cardId")
    suspend fun getFlashcardById(cardId: Int): Flashcard

    @Query("SELECT * FROM flashcard WHERE setId = :setId AND isStudied = 0 ORDER BY RANDOM() LIMIT 10")
    suspend fun getRandom10UnstudiedFlashcards(setId: Int): List<Flashcard>

    @Query("SELECT COUNT(*) FROM flashcard WHERE setId = :setId AND isStudied = 0")
    suspend fun getUnstudiedFlashcardsCount(setId: Int): Int
}
