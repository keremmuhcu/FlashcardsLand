package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashcardsViewModel(
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val setId = savedStateHandle.toRoute<Route.FlashcardsScreenRoute>().setId
    private val setTitle = savedStateHandle.toRoute<Route.FlashcardsScreenRoute>().setTitle

    private val _state = MutableStateFlow(FlashcardsState())
    val state = combine(
        _state,
        flashcardRepository.getFlashcardsBySetId(setId)
    ) { state, flashcards ->
        state.copy(
            flashcards = flashcards,
            isLoading = false,
            selectedSetTitle = setTitle,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FlashcardsState()
    )

    fun onEvent(event: FlashcardsEvent) {
        when(event){
            is FlashcardsEvent.ChangeFlashcardHardness -> changeFlashcardHardness(event.flashcard)
            is FlashcardsEvent.OnSegmentedButtonClicked -> {
                _state.update { it.copy(selectedSegmentButtonIndex = event.index) }
            }
            is FlashcardsEvent.OnSegmentedButtonClickedHard -> {
                _state.update { it.copy(selectedSegmentButtonIndexHard = event.index) }
            }

            is FlashcardsEvent.OnTabSelected -> {
                _state.update { it.copy(selectedTabIndex = event.index) }
            }

            is FlashcardsEvent.OnSearchBarTfChange -> {
                _state.update { it.copy(searchBarTextTf = event.search) }
            }

            is FlashcardsEvent.OnSearchIconClicked -> {
                _state.update { it.copy(searchBarActive = event.active) }
            }
        }
    }

    private fun changeFlashcardHardness(flashcard: Flashcard) {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(flashcard)
        }
    }
}

/*private fun loadFlashcards() {
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
   }*/