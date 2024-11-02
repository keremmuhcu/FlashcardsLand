package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards

sealed class SetListEvent {
    data object OnCreateSetButtonClicked : SetListEvent()
    data class OnSetTitleTextFieldChange(val title: TextFieldValue) : SetListEvent()
    data object OnDeleteSetButtonClicked : SetListEvent()
    data object OnEditSetButtonClicked : SetListEvent()
    data class ChangeSelectedSet(val selectedSet: FlashcardSetWithCards) : SetListEvent()
}