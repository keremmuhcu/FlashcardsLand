package com.keremmuhcu.flashcardsland.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainState(
    val isLoading: Boolean = true,
    val isDarkModeEnabled: Boolean = true
)

class MainViewModel(
    private val flashcardSetRepository: FlashcardSetRepository,
):ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState()
    )

    init {
        viewModelScope.launch {
            val isDark = flashcardSetRepository.getDarkMode()
            _state.update {
                it.copy(
                    isDarkModeEnabled = isDark,
                    isLoading = false
                )
            }
        }
    }

    fun darkModeChange() {
        viewModelScope.launch {
            val changeDarkMode = !state.value.isDarkModeEnabled
            flashcardSetRepository.updateDarkMode(changeDarkMode)
            _state.update { it.copy(isDarkModeEnabled = changeDarkMode) }
        }
    }
}