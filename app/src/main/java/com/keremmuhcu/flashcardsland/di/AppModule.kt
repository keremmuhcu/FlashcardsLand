package com.keremmuhcu.flashcardsland.di

import androidx.room.Room
import com.keremmuhcu.flashcardsland.data.local.AppDatabase
import com.keremmuhcu.flashcardsland.data.local.FlashcardDao
import com.keremmuhcu.flashcardsland.data.local.FlashcardSetDao
import com.keremmuhcu.flashcardsland.data.repository.FlashcardRepositoryImpl
import com.keremmuhcu.flashcardsland.data.repository.FlashcardSetRepositoryImpl
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single { get<AppDatabase>().flashcardDao() }
    single { get<AppDatabase>().flashcardSetDao() }
}

val repositoryModule = module {
    single<FlashcardRepository> { FlashcardRepositoryImpl(get()) }
    single<FlashcardSetRepository> { FlashcardSetRepositoryImpl(get()) }
}

val appModule = listOf(databaseModule, repositoryModule)