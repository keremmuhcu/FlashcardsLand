package com.keremmuhcu.flashcardsland.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet

@Database(entities = [Flashcard::class, FlashcardSet::class], version = 1, exportSchema = false)
@TypeConverters(ExampleSentencesListConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun flashcardSetDao(): FlashcardSetDao
}