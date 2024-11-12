package com.keremmuhcu.flashcardsland.presentation.study.basic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.domain.model.ListSortType
import com.keremmuhcu.flashcardsland.domain.model.StudyFlashcardsOrderType
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
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
                _state.value = _state.value.copy(
                    currentCardIndex = event.index
                )
            }

            BasicStudyEvent.OnFavoriteButtonClicked -> {
                _state.update {
                    it.copy(currentCardIsHard = !state.value.currentCardIsHard)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val settings = flashcardRepository.getFlashcardListFilters().first()
            _state.update {
                it.copy(settings = settings)
            }
            getRandomFlashcards()
        }
    }

    private fun updateStats(isCorrect: Boolean) {
        val currentCardIndex = state.value.currentCardIndex + 1
        val isFinish = state.value.currentCardIndex + 1 == state.value.flashcards.size
        val isHardWorkSection = state.value.settings.workHard
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                state.value.flashcards[state.value.currentCardIndex].copy(
                    isStudied = if (!isHardWorkSection) isCorrect else state.value.flashcards[state.value.currentCardIndex].isStudied,
                    isHard = state.value.currentCardIsHard,
                    isHardStudied = if (isHardWorkSection) isCorrect else state.value.flashcards[state.value.currentCardIndex].isHardStudied
                )
            )

            val remainingCards = if (isFinish) {
                if (state.value.settings.workHard)
                    flashcardRepository.getUnstudiedHardFlashcardsCount(setId)
                else
                    flashcardRepository.getUnstudiedFlashcardsCount(setId)
            } else {
                0
            }

            _state.value = state.value.copy(
                corrects = if (isCorrect) state.value.corrects + 1 else state.value.corrects,
                wrongs = if (!isCorrect) state.value.wrongs + 1 else state.value.wrongs,
                termSide = if (state.value.settings.workDefinitions) {
                    state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].definition
                } else {
                    state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].term
                },
                definitionSide = if (state.value.settings.workDefinitions) {
                    state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].term
                } else {
                    state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].definition
                },
                currentCardIsHard = state.value.flashcards[if (isFinish) currentCardIndex - 1 else currentCardIndex].isHard,
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
                currentCardIsHard = state.value.flashcards[0].isHard,
                currentCardIndex = 0,
                wrongs = 0,
                corrects = 0,
                isFinish = false
            )
        }
    }

    private fun getRandomFlashcards() {
        viewModelScope.launch {
            val sortOrder = getSortOrder()
            val flashcards = flashcardRepository.getUnstudiedFlashcardsForStudy(
                setId = setId,
                studyType = if (state.value.settings.workHard) "HARD" else "NORMAL",
                isHard = _state.value.settings.workHard,
                sortOrder = sortOrder,
                limit = _state.value.settings.tourCardCount
            )
            _state.update {
                it.copy(
                    flashcards = flashcards,
                    isLoading = false,
                    termSide = if (_state.value.settings.workDefinitions) {
                        flashcards.getOrNull(it.currentCardIndex)?.definition ?: ""
                    } else {
                        flashcards.getOrNull(it.currentCardIndex)?.term ?: ""
                    },
                    definitionSide = if (_state.value.settings.workDefinitions) {
                        flashcards.getOrNull(it.currentCardIndex)?.term ?: ""
                    } else {
                        flashcards.getOrNull(it.currentCardIndex)?.definition ?: ""
                    },
                    currentCardIsHard = flashcards.getOrNull(it.currentCardIndex)?.isHard ?: false
                )
            }
        }
    }

    private fun getSortOrder(): StudyFlashcardsOrderType {
        return when(_state.value.settings.studySortType) {
            ListSortType.RANDOM -> StudyFlashcardsOrderType.RANDOM
            ListSortType.DATE_ASCENDING -> StudyFlashcardsOrderType.CREATED_DATE_ASC
            ListSortType.DATE_DESCENDING -> StudyFlashcardsOrderType.CREATED_DATE_DESC
            ListSortType.ALPHABETICAL_ASCENDING -> {
                if (_state.value.settings.workDefinitions)
                    StudyFlashcardsOrderType.DEFINITION_ASC
                else
                    StudyFlashcardsOrderType.TERM_ASC
            }
            ListSortType.ALPHABETICAL_DESCENDING -> {
                if (_state.value.settings.workDefinitions)
                    StudyFlashcardsOrderType.DEFINITION_DESC
                else
                    StudyFlashcardsOrderType.TERM_DESC
            }
            else -> StudyFlashcardsOrderType.TERM_DESC
        }
    }
}