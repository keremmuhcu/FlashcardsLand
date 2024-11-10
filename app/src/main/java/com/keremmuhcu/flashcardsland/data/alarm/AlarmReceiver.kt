package com.keremmuhcu.flashcardsland.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.keremmuhcu.flashcardsland.data.local.FlashcardDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class AlarmReceiver: BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent?) {
        // Alarm tetiklendiğinde veritabanında reset işlemini gerçekleştirin
        val flashcardDao: FlashcardDao by inject()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Alarm", "Alarm alındı.")
            flashcardDao.resetAllProgress()
        }
    }
}