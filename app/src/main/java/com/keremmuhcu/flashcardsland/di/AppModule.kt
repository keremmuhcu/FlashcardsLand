package com.keremmuhcu.flashcardsland.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.keremmuhcu.flashcardsland.data.local.AppDatabase
import com.keremmuhcu.flashcardsland.data.repository.FlashcardRepositoryImpl
import com.keremmuhcu.flashcardsland.data.repository.FlashcardSetRepositoryImpl
import com.keremmuhcu.flashcardsland.domain.model.Settings
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardViewModel
import com.keremmuhcu.flashcardsland.presentation.flashcards.FlashcardsViewModel
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListViewModel
import com.keremmuhcu.flashcardsland.presentation.main.MainViewModel
import com.keremmuhcu.flashcardsland.presentation.study.basic.BasicStudyViewModel
import com.keremmuhcu.flashcardsland.presentation.study.multiple_answers.MultipleAnswersStudyViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app_database"
        )
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch {
                        get<AppDatabase>().settingsDao().populateData(Settings())
                    }
                }
            })
            .build()
    }


    single { get<AppDatabase>().flashcardDao() }
    single { get<AppDatabase>().flashcardSetDao() }
    single { get<AppDatabase>().settingsDao() }
}

val repositoryModule = module {
    single<FlashcardRepository> { FlashcardRepositoryImpl(get(), get(), get()) }
    single<FlashcardSetRepository> { FlashcardSetRepositoryImpl(get(), get(), get ()) }
}
val viewModelModule = module {
    viewModel { SetListViewModel(get()) }
    viewModel { AddOrEditFlashcardViewModel(get(), get()) }
    viewModel { FlashcardsViewModel(get(),get()) }
    viewModel { BasicStudyViewModel(get(),get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MultipleAnswersStudyViewModel(get(), get()) }
}


val appModule = listOf(databaseModule, repositoryModule, viewModelModule)