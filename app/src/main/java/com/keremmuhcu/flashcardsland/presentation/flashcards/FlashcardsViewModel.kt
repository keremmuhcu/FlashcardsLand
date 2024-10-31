package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashcardsViewModel(
    private val flashcardRepository: FlashcardRepository,
): ViewModel() {
    private val _state = MutableStateFlow(FlashcardsState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            FlashcardsState()
        )

    fun onEvent(event: FlashcardsEvent) {
        when(event){
            // todo: add event handlers
            else -> {}
        }
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            flashcardRepository.getFlashcardsBySetId(_state.value.selectedSetId)
                .collect { flashcards->
                    _state.update {
                        it.copy(
                            flashcards = flashcards,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun loadData(setId: Int, setTitle: String) {
        _state.update {
            it.copy(
                selectedSetId = setId,
                selectedSetTitle = setTitle
            )
        }

        loadFlashcards()
    }
}