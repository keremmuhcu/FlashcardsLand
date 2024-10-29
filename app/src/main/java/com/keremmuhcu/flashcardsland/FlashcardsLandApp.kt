package com.keremmuhcu.flashcardsland

import android.app.Application
import com.keremmuhcu.flashcardsland.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FlashcardsLandApp: Application()  {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlashcardsLandApp)
            androidLogger()
            modules(appModule)
        }
    }
}