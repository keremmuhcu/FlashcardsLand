package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.compose.ui.text.input.TextFieldValue
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards

sealed class SetListEvent {
    data object OnCreateSetButtonClicked : SetListEvent()
    data class OnSetTitleTextFieldChange(val title: TextFieldValue) : SetListEvent()
    data object OnDeleteSetButtonClicked : SetListEvent()
    data object OnEditSetButtonClicked : SetListEvent()
    data class ChangeSelectedSet(val selectedSet: FlashcardSetWithCards) : SetListEvent()
    data class OnResetProgressButtonClicked(val setId: Int) : SetListEvent()
    // study filters
    data class OnStudySortTypeChanged(val sortType: String) : SetListEvent()
    data class OnCardCountOneRoundChanged(val cardCountOneRound: String) : SetListEvent()
    data object OnWorkOnlyHardSwitchClicked : SetListEvent()
    data object OnWorkDefinitionsSwitchesClicked : SetListEvent()
    data object OnFiltersConfirmButtonClicked : SetListEvent()
    data object OnFiltersCancelButtonClicked : SetListEvent()
}