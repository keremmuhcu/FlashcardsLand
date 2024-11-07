package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSet
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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
    ) { state, setsWithCards ->
        state.copy(
            flashcardSets = setsWithCards,
            isLoading = false
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