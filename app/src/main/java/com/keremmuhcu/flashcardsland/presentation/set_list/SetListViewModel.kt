package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetListViewModel(
    private val flashcardSetRepository: FlashcardSetRepository
): ViewModel() {
    private val _state = MutableStateFlow(SetListState())
    val state = combine(
        _state,
        flashcardSetRepository.getAllFlashcardSetsWithCards()
    ) { state, setsWithCards,  ->
        state.copy(
            flashcardSets = setsWithCards,
            isLoading = state.studySortType.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SetListState()
    )

    fun onEvent(event: SetListEvent) {
        when (event) {
            SetListEvent.OnCreateSetButtonClicked -> createSet()
            SetListEvent.OnEditSetButtonClicked -> updateSet()
            SetListEvent.OnDeleteSetButtonClicked -> deleteSet()
            is SetListEvent.OnSetTitleTextFieldChange -> {
                _state.update {
                    it.copy(setTitleTextField = event.title)
                }
            }
            is SetListEvent.ChangeSelectedSet -> {
                _state.update {
                    it.copy(selectedSet = event.selectedSet)
                }
            }

            is SetListEvent.OnCardCountOneRoundChanged -> {
                _state.update { it.copy(cardCountOneRound = event.cardCountOneRound) }
            }
            is SetListEvent.OnStudySortTypeChanged -> {
                _state.update { it.copy(studySortType = event.sortType) }
            }
            SetListEvent.OnWorkDefinitionsSwitchesClicked -> {
                _state.update { it.copy(workDefinitions = !_state.value.workDefinitions) }
            }
            SetListEvent.OnWorkOnlyHardSwitchClicked -> {
                viewModelScope.launch {
                    flashcardSetRepository.updateWorkHard(!state.value.workOnlyHard)
                    _state.update { it.copy(workOnlyHard = !_state.value.workOnlyHard) }
                }
            }

            SetListEvent.OnFiltersCancelButtonClicked -> resetFields()
            SetListEvent.OnFiltersConfirmButtonClicked -> {
                viewModelScope.launch {
                    val settings = state.value.settings.copy(
                        tourCardCount = state.value.cardCountOneRound.toInt(),
                        studySortType = state.value.studySortType,
                        workDefinitions = state.value.workDefinitions,
                        workHard = state.value.workOnlyHard
                    )
                    flashcardSetRepository.updateSettings(settings)
                    _state.update { it.copy(settings = settings) }
                }
            }

            is SetListEvent.OnResetProgressButtonClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(resetProgress = true) }
                    if (state.value.workOnlyHard)
                        flashcardSetRepository.resetHardProgress(setId = event.setId)
                    else
                        flashcardSetRepository.resetNormalProgress(setId = event.setId)

                    _state.update { it.copy(resetProgress = false) }

                }
            }
        }
    }

    init {
        getDefaultValues()
    }

    private fun getDefaultValues() {
        viewModelScope.launch {
            val settings = flashcardSetRepository.getFlashcardListFilters().first()
            _state.update { it.copy(settings = settings) }

            resetFields()
        }
    }

    private fun resetFields() {
        _state.update {
            it.copy(
                studySortType = _state.value.settings.studySortType,
                cardCountOneRound = _state.value.settings.tourCardCount.toString(),
                workDefinitions = _state.value.settings.workDefinitions,
                workOnlyHard = _state.value.settings.workHard
            )
        }
    }


    private fun updateSet() {
        viewModelScope.launch {
            _state.value.selectedSet?.let {
                val set = it.flashcardSet
                flashcardSetRepository.upsertFlashcardSet(
                    flashcardSet = FlashcardSet(
                        setId = set.setId,
                        title = state.value.setTitleTextField.text.trim(),
                        createdAt = set.createdAt,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun deleteSet() {
        viewModelScope.launch {
            _state.value.selectedSet?.let {
                flashcardSetRepository.deleteFlashcardSet(it.flashcardSet)
            }
        }
    }

    private fun createSet() {
        viewModelScope.launch {
            flashcardSetRepository.upsertFlashcardSet(
                flashcardSet = FlashcardSet(
                    title = state.value.setTitleTextField.text.trim()
                )
            )
        }
    }
}