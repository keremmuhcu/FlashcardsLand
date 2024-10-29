package com.keremmuhcu.flashcardsland.data.local

import androidx.room.TypeConverter

class ExampleSentencesListConverter {
    private val separator = "::"
    @TypeConverter
    fun fromExampleList(value: List<String>): String {
        return value.joinToString(separator)
    }
    @TypeConverter
    fun toExampleList(value: String): List<String> {
        return value.split(separator).map { it.trim() }
    }

}