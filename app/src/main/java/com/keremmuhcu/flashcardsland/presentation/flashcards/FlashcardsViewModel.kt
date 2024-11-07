package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.ListSortType
import com.keremmuhcu.flashcardsland.domain.model.Settings
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
        flashcardRepository.getFlashcardsBySetId(setId),
        flashcardRepository.getFlashcardListFilters()
    ) { state, flashcards, settings ->
        println("Flash viewmodel statecombine girdi..")
        val filteredList = when(settings.listSortType) {
            ListSortType.ALPHABETICAL_ASCENDING -> flashcards.sortedBy { if (settings.showOnlyTerm) it.term else it.definition }
            ListSortType.ALPHABETICAL_DESCENDING -> flashcards.sortedByDescending { if (settings.showOnlyTerm) it.term else it.definition }
            ListSortType.DATE_ASCENDING -> flashcards.sortedBy { it.createdDate }
            ListSortType.DATE_DESCENDING -> flashcards.sortedByDescending { it.createdDate }
            else -> flashcards.sortedBy { it.createdDate }
        }
        state.copy(
            flashcards = filteredList,
            settings = settings,
            isLoading = false,
            selectedSetTitle = setTitle,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FlashcardsState()
    )

    init {
        getDefaultValues()
    }

    private fun getDefaultValues() {
        viewModelScope.launch {
            val settings = flashcardRepository.getFlashcardListFilters().first()
            _state.update {
                it.copy(
                    selectedSortType = settings.listSortType,
                    showDateSwitch = settings.showDate,
                    showOneSideSwitch = settings.showOneSide,
                    showOnlyTermRadioButton = settings.showOnlyTerm,
                    canCardFlip = settings.cardCanFlip
                )
            }
        }
    }

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

            FlashcardsEvent.OnCanCardFlipCheckBoxClicked -> _state.update { it.copy(canCardFlip = !_state.value.canCardFlip) }
            FlashcardsEvent.OnConfirmFiltersButtonClicked -> {
                viewModelScope.launch {
                    flashcardRepository.updateSettings(
                        state.value.settings.copy(
                            listSortType = state.value.selectedSortType,
                            showDate = state.value.showDateSwitch,
                            showOneSide = state.value.showOneSideSwitch,
                            showOnlyTerm = state.value.showOnlyTermRadioButton,
                            cardCanFlip = state.value.canCardFlip
                        )
                    )
                }
            }
            FlashcardsEvent.OnDismissRequestClicked -> {
                _state.update {
                    it.copy(
                        selectedSortType = state.value.settings.listSortType,
                        showDateSwitch = state.value.settings.showDate,
                        showOneSideSwitch = state.value.settings.showOneSide,
                        showOnlyTermRadioButton = state.value.settings.showOnlyTerm,
                        canCardFlip = state.value.settings.cardCanFlip
                    )
                }
            }
            is FlashcardsEvent.OnDropdownItemClicked -> {
                _state.update { it.copy(selectedSortType = event.item) }
            }
            is FlashcardsEvent.OnRadioButtonClicked -> {
                _state.update { it.copy(showOnlyTermRadioButton = event.isTermClicked) }
            }
            FlashcardsEvent.OnShowDateSwitchClicked -> _state.update { it.copy(showDateSwitch = !_state.value.showDateSwitch) }
            FlashcardsEvent.OnShowOneSideSwitchClicked -> {
                _state.update { it.copy(showOneSideSwitch = !_state.value.showOneSideSwitch) }
            }

            is FlashcardsEvent.OnDeleteItemClicked -> {
                viewModelScope.launch {
                    flashcardRepository.deleteFlashcardById(event.cardId)
                }
            }
        }
    }

    private fun changeFlashcardHardness(flashcard: Flashcard) {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(flashcard)
        }
    }
}