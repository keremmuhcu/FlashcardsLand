package com.keremmuhcu.flashcardsland.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keremmuhcu.flashcardsland.domain.model.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Insert
    suspend fun populateData(settings: Settings)

    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<Settings>

    @Query("SELECT isDarkMode FROM settings WHERE id = 1")
    suspend fun getDarkMode(): Boolean

    @Query("UPDATE settings SET isDarkMode = :isDarkMode WHERE id = 1")
    suspend fun updateDarkMode(isDarkMode: Boolean)

    @Update
    suspend fun updateSettings(settings: Settings)

}