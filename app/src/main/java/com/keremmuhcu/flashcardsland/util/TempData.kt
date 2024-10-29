package com.keremmuhcu.flashcardsland.util

import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards

val set1FlashcardsList = listOf(
    Flashcard(
        setId = 1,
        term = "Hello",
        definition = "Merhaba",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        term = "World",
        definition = "Dünya",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        term = "Computer",
        definition = "Bilgisayar",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        term = "Programming",
        definition = "Programlama",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        term = "Language",
        definition = "Dil",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        term = "School",
        definition = "Okul",
        isStudied = true,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
        term = "University",
        definition = "Üniversite",
        isStudied = false,
        isHard = false,
        isHardStudied = false
    ),
    Flashcard(
        setId = 1,
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

