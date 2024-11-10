package com.keremmuhcu.flashcardsland.presentation.study.basic


sealed class BasicStudyEvent {
    data object OnCorrectButtonClicked: BasicStudyEvent()
    data object OnWrongButtonClicked: BasicStudyEvent()
    data object OnStartNextRoundClicked: BasicStudyEvent()
    data object OnRepeatCurrentRoundClicked: BasicStudyEvent()
    data class ChangeCurrentCardIndex(val index: Int): BasicStudyEvent()
    data object OnFavoriteButtonClicked: BasicStudyEvent()

}