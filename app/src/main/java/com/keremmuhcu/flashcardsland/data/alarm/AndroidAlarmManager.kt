package com.keremmuhcu.flashcardsland.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AndroidAlarmManager(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm() {
        val calendar = Calendar.getInstance().apply {
            // Şu anki zamanı alıyoruz
            timeInMillis = System.currentTimeMillis()

            // Eğer şu anki saat 5'ten küçükse, alarmı bugüne kuruyoruz
            // Eğer şu anki saat 5'ten büyükse, alarmı bir sonraki güne kuruyoruz
            if (get(Calendar.HOUR_OF_DAY) >= 5) {
                add(Calendar.DAY_OF_YEAR, 1)  // Bir sonraki güne kur
            }

            // Alarmı 5:00'te çalacak şekilde ayarla
            set(Calendar.HOUR_OF_DAY, 5)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}