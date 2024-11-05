package com.keremmuhcu.flashcardsland.presentation.study.basic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.room.util.copy
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BasicStudyViewModel(
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    private val setId = savedStateHandle.toRoute<Route.BasicStudyScreenRoute>().setId
    private val _state = MutableStateFlow(BasicStudyState())
    val state = _state
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BasicStudyState()
        )

    fun onEvent(event: BasicStudyEvent) {
        when(event) {
            is BasicStudyEvent.OnCorrectButtonClicked -> updateStats(true)

            BasicStudyEvent.OnWrongButtonClicked -> updateStats(false)


            BasicStudyEvent.OnStartNextRoundClicked -> startNextRound()

            BasicStudyEvent.OnRepeatCurrentRoundClicked -> repeatCurrentRound()

            is BasicStudyEvent.ChangeCurrentCardIndex -> {
                _state.value = state.value.copy(
                    currentCardIndex = event.index
                )
            }
        }
    }

    init {
        getRandomFlashcards()
    }

    private fun updateStats(isCorrect: Boolean) {
        val currentCardIndex = state.value.currentCardIndex + 1
        val isFinish = currentCardIndex == state.value.flashcards.size
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                state.value.flashcards[state.value.currentCardIndex].copy(
                    isStudied = isCorrect
                )
            )
            val remainingCards = if (isFinish) flashcardRepository.getUnstudiedFlashcardsCount(setId) else 0
            _state.value = state.value.copy(
                corrects = if (isCorrect) state.value.corrects + 1 else state.value.corrects,
                wrongs = if (!isCorrect) state.value.wrongs + 1 else state.value.wrongs,
                termSide = state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].term,//state.value.flashcards.getOrNull(currentCardIndex)?.term ?: "",
                definitionSide = state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].definition,//state.value.flashcards.getOrNull(currentCardIndex)?.definition ?: "",
                isFinish = isFinish,
                remainingCards = remainingCards
            )
        }
    }

    private fun startNextRound() {
        _state.update {
            it.copy(
                isLoading = true,
                currentCardIndex = 0,
                wrongs = 0,
                corrects = 0,
                isFinish = false
            )
        }
        getRandomFlashcards()
    }

    private fun repeatCurrentRound() {
        _state.update {
            it.copy(
                flashcards = state.value.flashcards,
                termSide = state.value.flashcards[0].term,
                definitionSide = state.value.flashcards[0].definition,
                currentCardIndex = 0,
                wrongs = 0,
                corrects = 0,
                isFinish = false
            )
        }
    }

    private fun getRandomFlashcards() {
        viewModelScope.launch {
            val flashcards = flashcardRepository.getRandom10UnstudiedFlashcards(setId)
            //val remainingCards = flashcardRepository.getUnstudiedFlashcardsCount(setId)
            _state.update {
                it.copy(
                    flashcards = flashcards,
                    isLoading = false,
                    termSide = flashcards.getOrNull(it.currentCardIndex)?.term ?: "",
                    definitionSide = flashcards.getOrNull(it.currentCardIndex)?.definition ?: "",
                )
            }
        }
    }
}