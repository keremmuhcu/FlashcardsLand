package com.keremmuhcu.flashcardsland.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 1,
    val isDarkMode: Boolean = false,
    val listSortType: String = ListSortType.DATE_DESCENDING,
    val showDate: Boolean = false,
    val showOneSide: Boolean = false,
    val showOnlyTerm: Boolean = true,
    val cardCanFlip: Boolean = true,
    // study filters
    val studySortType: String = ListSortType.DATE_DESCENDING,
    val tourCardCount: Int = 10,
    val workHard : Boolean = false,
    val workDefinitions: Boolean = false
)
