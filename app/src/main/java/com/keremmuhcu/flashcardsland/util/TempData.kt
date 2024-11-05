package com.keremmuhcu.flashcardsland.util

import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards

val set1FlashcardsList = listOf(
    Flashcard(
        setId = 1,
        cardId = 1,
        term = "Hello",
        definition = "Merhaba",
        isStudied = false,
        isHard = false,
        isHardStudied = false,
        examples = listOf("Bu birinci kartın 1. örneği", "Bu birinci kartın 2. örneği", "Bu birinci kartın 3. örneği")
    ),
    Flashcard(
        setId = 1,
        cardId = 2,
        term = "World",
        definition = "Dünya",
        isStudied = false,
        isHard = false,
        isHardStudied = false,
        examples = listOf("Bu ikinci kartın 1. örneği", "Bu ikinci kartın 2. örneği")
    ),
    Flashcard(
        setId = 1,
        cardId = 3,
        term = "Computer",
        definition = "Bilgisayar",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        cardId = 4,
        term = "Programming",
        definition = "Programlama",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        cardId = 5,
        term = "Language",
        definition = "Dil",
        isStudied = true,
        isHard = false,
        isHardStudied = false,
        examples = listOf("Bu beşinci kartın 1. örneği")
    ),
    Flashcard(
        setId = 1,
        cardId = 6,
        term = "School",
        definition = "Okul",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        cardId = 7,
        term = "University",
        definition = "Üniversite",
        isStudied = false,
        isHard = false,
        isHardStudied = false,
        examples = listOf("Bu yedinci kartın 1. örneği", "Bu yedinci kartın 2. örneği", "Bu birinci kartın 3. örneği")
    ),
    Flashcard(
        setId = 1,
        cardId = 8,
        term = "Teacher",
        definition = "Öğretmen",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    )
)

val set2FlashcardsList = listOf(
    Flashcard(
        setId = 2,
        term = "Hello",
        definition = "Merhaba",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "World",
        definition = "Dünya",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Computer",
        definition = "Bilgisayar",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Programming",
        definition = "Programlama",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Language",
        definition = "Dil",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "School",
        definition = "Okul",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "University",
        definition = "Üniversite",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Teacher",
        definition = "Öğretmen",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Student",
        definition = "Öğrenci",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Teacher",
        definition = "Öğretmen",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    )
)

val set3FlashcardsList = listOf(
    Flashcard(
        setId = 2,
        term = "Hello",
        definition = "Merhaba",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "World",
        definition = "Dünya",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 2,
        term = "Computer",
        definition = "Bilgisayar",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    )
)

val setList = listOf(
    FlashcardSetWithCards(
        FlashcardSet(
            setId = 1,
            title = "İngilizce - Türkçe - Vocabulary A2 Seviye"
        ),
        cards = set1FlashcardsList
    ),
    FlashcardSetWithCards(
        FlashcardSet(
            setId = 2,
            title = "Tarih"
        ),
        cards = set2FlashcardsList
    ),
    FlashcardSetWithCards(
        FlashcardSet(
            setId = 3,
            title = "Fen Bilimleri"
        ),
        cards = set3FlashcardsList
    )
)

