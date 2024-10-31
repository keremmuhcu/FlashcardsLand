package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import com.keremmuhcu.flashcardsland.domain.model.Flashcard

data class AddOrEditFlashcardState(
    val termTf: String = "",
    val definitionTf: String = "",
    val examplesTfList: List<String> = listOf(""),
    val isHardSwitchChecked: Boolean = false,
    val isExampleSwitchChecked: Boolean = false,
    val selectedFlashcard: Flashcard? = null,
    val selectedSetId: Int? = null,
    val buttonsActivityControl: Boolean = false,
    val isSuccessful: Boolean = false
)
