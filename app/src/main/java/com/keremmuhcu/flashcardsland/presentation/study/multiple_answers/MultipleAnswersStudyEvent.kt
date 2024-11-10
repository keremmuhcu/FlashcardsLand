package com.keremmuhcu.flashcardsland.presentation.study.multiple_answers

import com.keremmuhcu.flashcardsland.presentation.study.basic.BasicStudyEvent

sealed class MultipleAnswersStudyEvent {
    data class OnOptionClicked(val isTrue: Boolean): MultipleAnswersStudyEvent()
    data object OnStartNextRoundClicked: MultipleAnswersStudyEvent()
    data object OnRepeatCurrentRoundClicked: MultipleAnswersStudyEvent()
    data class ChangeCurrentCardIndex(val index: Int): MultipleAnswersStudyEvent()
    data object OnFavoriteButtonClicked: MultipleAnswersStudyEvent()
}