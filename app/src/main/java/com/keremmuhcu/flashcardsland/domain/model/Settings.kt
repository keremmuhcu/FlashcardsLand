package com.keremmuhcu.flashcardsland.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 1,
    val isDarkMode: Boolean = false,
    val listSortType: String = ListSortType.ALPHABETICAL_ASCENDING,
    val showDate: Boolean = false,
    val showOneSide: Boolean = false,
    val showOnlyTerm: Boolean = true,
    val cardCanFlip: Boolean = true,
    val studySortType: String = StudySortType.ALPHABETICAL_ASCENDING,
    val tourCardCount: Int = 5,
)
