package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.Flashcard

data class AddOrEditFlashcardState(
    val termTf: TextFieldValue = TextFieldValue(""),
    val definitionTf: TextFieldValue = TextFieldValue(""),
    val examplesTfList: List<TextFieldValue> = listOf(TextFieldValue("")),
    val isHardSwitchChecked: Boolean = false,
    val isExampleSwitchChecked: Boolean = false,
    val selectedFlashcard: Flashcard? = null,
    val selectedSetId: Int = -1,
    val buttonsActivityControl: Boolean = false,
    val isSuccessful: Boolean = false,
)
