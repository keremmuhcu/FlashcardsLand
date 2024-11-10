package com.keremmuhcu.flashcardsland.presentation.study.multiple_answers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
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

class MultipleAnswersStudyViewModel(
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    private val setId = savedStateHandle.toRoute<Route.MultipleAnswersScreenRoute>().setId
    private val _state = MutableStateFlow(MultipleAnswersStudyState())
    val state = _state
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MultipleAnswersStudyState()
        )

    fun onEvent(event: MultipleAnswersStudyEvent) {
        when(event) {
            is MultipleAnswersStudyEvent.ChangeCurrentCardIndex -> {
                _state.value = state.value.copy(currentCardIndex = event.index)
            }
            is MultipleAnswersStudyEvent.OnOptionClicked -> {
                updateStats(event.isTrue)
                if (state.value.currentCardIndex < state.value.flashcards.size - 1) {
                    _state.update {
                        it.copy(currentCardIndex = state.value.currentCardIndex + 1)
                    }
                }
            }
            MultipleAnswersStudyEvent.OnRepeatCurrentRoundClicked -> repeatCurrentRound()
            MultipleAnswersStudyEvent.OnStartNextRoundClicked -> startNextRound()
            MultipleAnswersStudyEvent.OnFavoriteButtonClicked -> {
                viewModelScope.launch {
                    flashcardRepository.upsertFlashcard(
                        state.value.flashcards[state.value.currentCardIndex].copy(
                            isHard = !state.value.currentCardIsHard
                        )
                    )
                    _state.update {
                        it.copy(currentCardIsHard = !state.value.currentCardIsHard)
                    }
                }
            }
        }
    }

    private fun updateStats(isCorrect: Boolean) {
        val isFinish = state.value.currentCardIndex + 1 >= state.value.flashcards.size
        val isHardWorkSection = state.value.settings.workHard

        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                state.value.flashcards[state.value.currentCardIndex].copy(
                    isStudied = if (!isHardWorkSection) isCorrect else state.value.flashcards[state.value.currentCardIndex].isStudied,
                    isHard = state.value.currentCardIsHard,
                    isHardStudied = if (isHardWorkSection) isCorrect else state.value.flashcards[state.value.currentCardIndex].isHardStudied
                )
            )

            val remainingCards = if (isFinish) flashcardRepository.getUnstudiedFlashcardsCount(setId) else 0
            _state.value = state.value.copy(
                corrects = if (isCorrect) state.value.corrects + 1 else state.value.corrects,
                currentCardIsHard = if (!isFinish && state.value.flashcards.isNotEmpty()) state.value.flashcards[state.value.currentCardIndex].isHard else false,
                question = if (!isFinish && state.value.flashcards.isNotEmpty()) {
                    if (state.value.settings.workDefinitions) state.value.flashcards[state.value.currentCardIndex].definition else state.value.flashcards[state.value.currentCardIndex].term
                } else {
                    ""
                },
                wrongs = if (!isCorrect) state.value.wrongs + 1 else state.value.wrongs,
                options = if (!isFinish && state.value.flashcards.size > 1) {
                    (state.value.answers.subList((state.value.currentCardIndex + 1) * 3 - 3, ((state.value.currentCardIndex + 1) * 3)) + if (state.value.settings.workDefinitions) state.value.flashcards[state.value.currentCardIndex].term else state.value.flashcards[state.value.currentCardIndex].definition).shuffled()
                } else listOf(),
                isFinish = isFinish,
                remainingCards = remainingCards
            )
        }
    }

    init {
        viewModelScope.launch {
            val settings = flashcardRepository.getFlashcardListFilters().first()
            _state.update { it.copy(settings = settings) }
            getRandomFlashcards()
        }
    }

    private fun startNextRound() {
        _state.update {
            it.copy(
                isLoading = true,
                currentCardIndex = 0,
                question = "",
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
                question = state.value.flashcards[0].term,
                options = (state.value.answers.subList(0,3) + if (state.value.settings.workDefinitions) state.value.flashcards[0].term else state.value.flashcards[0].definition).shuffled(),
                currentCardIndex = 0,
                currentCardIsHard = state.value.flashcards[0].isHard,
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
                isHard = state.value.settings.workHard,
                sortOrder = sortOrder,
                limit = state.value.settings.tourCardCount
            )
            println(flashcards.size)
            val answers :MutableList<String> = mutableListOf()
            flashcards.forEach { card ->
                answers += flashcardRepository.getFalseAnswers(setId, card.cardId!!).map {
                    if (state.value.settings.workDefinitions) it.term else it.definition
                }
            }

            //println(state.value.flashcards[0].term)
            _state.update {
                it.copy(
                    flashcards = flashcards,
                    question = if (state.value.settings.workDefinitions) flashcards[state.value.currentCardIndex].definition else flashcards[state.value.currentCardIndex].term,
                    currentCardIsHard = flashcards[state.value.currentCardIndex].isHard,
                    answers = answers,
                    options = (answers.subList((state.value.currentCardIndex + 1) * 3 - 3, ((state.value.currentCardIndex + 1) * 3)) + if (state.value.settings.workDefinitions) flashcards[state.value.currentCardIndex].term else flashcards[state.value.currentCardIndex].definition).shuffled(),
                    isLoading = false
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