package com.keremmuhcu.flashcardsland.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.Settings

@Database(entities = [Flashcard::class, FlashcardSet::class, Settings::class], version = 1, exportSchema = false)
@TypeConverters(ExampleSentencesListConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun flashcardSetDao(): FlashcardSetDao
    abstract fun settingsDao(): SettingsDao
}