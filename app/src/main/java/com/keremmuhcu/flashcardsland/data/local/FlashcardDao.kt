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

    @Query("""
    SELECT * FROM flashcard 
    WHERE setId = :setId AND 
    CASE 
        WHEN :studyType = 'NORMAL' THEN isStudied = 0
        WHEN :studyType = 'HARD' THEN isHardStudied = 0
    END
    AND (:isHard = 0 OR isHard = :isHard)
    ORDER BY 
    CASE WHEN :sortOrder = 'RANDOM' THEN RANDOM() END,
    CASE WHEN :sortOrder = 'TERM_ASC' THEN term END ASC,
    CASE WHEN :sortOrder = 'TERM_DESC' THEN term END DESC,
    CASE WHEN :sortOrder = 'DEFINITION_ASC' THEN definition END ASC,
    CASE WHEN :sortOrder = 'DEFINITION_DESC' THEN definition END DESC,
    CASE WHEN :sortOrder = 'CREATED_DATE_ASC' THEN createdDate END ASC,
    CASE WHEN :sortOrder = 'CREATED_DATE_DESC' THEN createdDate END DESC
    LIMIT :limit
""")
    suspend fun getUnstudiedFlashcardsForStudy(
        setId: Int,
        studyType: String,  // 'NORMAL' veya 'HARD'
        isHard: Boolean,
        sortOrder: String,
        limit: Int
    ): List<Flashcard>

    @Query("DELETE FROM flashcard WHERE cardId = :cardId")
    suspend fun deleteFlashcardById(cardId: Int)

    @Query("SELECT COUNT(*) FROM flashcard WHERE setId = :setId AND isStudied = 0")
    suspend fun getUnstudiedFlashcardsCount(setId: Int): Int

    @Query("SELECT COUNT(*) FROM flashcard WHERE setId = :setId AND isHardStudied = 0 AND isHard = 1")
    suspend fun getUnstudiedHardFlashcardsCount(setId: Int): Int

    @Query("SELECT * FROM flashcard WHERE setId = :setId AND cardId NOT IN (:cardId) ORDER BY RANDOM() LIMIT 3")
    suspend fun getFalseAnswers(setId: Int, cardId: Int): List<Flashcard>

    @Query("UPDATE flashcard SET isStudied = 0 WHERE setId = :setId")
    suspend fun resetNormalProgress(setId: Int)

    @Query("UPDATE flashcard SET isHardStudied = 0 WHERE setId = :setId")
    suspend fun resetHardProgress(setId: Int)

    @Query("UPDATE flashcard SET isHardStudied = 0, isStudied = 0")
    suspend fun resetAllProgress()
}
