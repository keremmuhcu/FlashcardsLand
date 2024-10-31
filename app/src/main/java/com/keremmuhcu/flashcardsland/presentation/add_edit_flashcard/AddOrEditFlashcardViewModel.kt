package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddOrEditFlashcardViewModel(
    private val flashcardRepository: FlashcardRepository
): ViewModel() {
    private val _state = MutableStateFlow(AddOrEditFlashcardState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AddOrEditFlashcardState()
        )

    fun onEvent(event: AddOrEditFlashcardEvent) {
        when (event) {
            AddOrEditFlashcardEvent.OnDeleteButtonClicked -> TODO()
            AddOrEditFlashcardEvent.OnExampleSwitchChange -> {
                _state.update {
                    it.copy(isExampleSwitchChecked = !it.isExampleSwitchChecked)
                }
            }
            AddOrEditFlashcardEvent.OnHardSwitchChange -> {
                _state.update {
                    it.copy(isHardSwitchChecked = !it.isHardSwitchChecked)
                }
            }
            AddOrEditFlashcardEvent.OnSaveButtonClicked -> addFlashcard()
            AddOrEditFlashcardEvent.OnAddExampleIconClicked -> {
                _state.update {
                    it.copy(examplesTfList = it.examplesTfList + "")
                }
            }
            is AddOrEditFlashcardEvent.OnDefinitionTextFieldChange -> {
                _state.update {
                    it.copy(definitionTf = event.definition)
                }
            }
            is AddOrEditFlashcardEvent.OnExampleTextFieldChange -> {
                _state.update {
                    it.copy(examplesTfList = it.examplesTfList.mapIndexed { index, s ->
                        if (index == event.index) event.example else s
                    })
                }
            }
            is AddOrEditFlashcardEvent.OnTermTextFieldChange -> {
                _state.update {
                    it.copy(termTf = event.term)
                }
            }
            is AddOrEditFlashcardEvent.OnDeleteExampleIconClicked -> {
                _state.update {
                    it.copy(examplesTfList = it.examplesTfList.toMutableList().apply {
                        removeAt(event.index)
                    })
                }
            }

            AddOrEditFlashcardEvent.OnSaveAndNextButtonClicked -> addAndGoNext()
        }
    }

    private fun addAndGoNext() {
        viewModelScope.launch {
            _state.value.selectedSetId?.let { setId->
                flashcardRepository.upsertFlashcard(
                    flashcard = Flashcard(
                        setId = setId,
                        term = state.value.termTf.trim(),
                        definition = state.value.definitionTf.trim(),
                        isHard = state.value.isHardSwitchChecked,
                        isStudied = false,
                        isHardStudied = false,
                        examples = if (state.value.isExampleSwitchChecked) state.value.examplesTfList.map { it.trim() } else emptyList()
                    )
                )
                resetFields()
            }
        }
    }

    private fun resetFields() {
        _state.update {
            it.copy(
                termTf = "",
                definitionTf = "",
                isHardSwitchChecked = false,
                isExampleSwitchChecked = false,
                examplesTfList = listOf(""),
                isSuccessful = true
            )
        }
    }

    private fun addFlashcard() {
        viewModelScope.launch {
            _state.value.selectedSetId?.let { setId->
                flashcardRepository.upsertFlashcard(
                    flashcard = Flashcard(
                        setId = setId,
                        term = state.value.termTf,
                        definition = state.value.definitionTf,
                        isHard = state.value.isHardSwitchChecked,
                        isStudied = false,
                        isHardStudied = false,
                        examples = if (state.value.isExampleSwitchChecked) state.value.examplesTfList else emptyList()
                    )
                )
            }
        }

    }

    fun setSelectedSetId(setId: Int) {
        _state.update {
            it.copy(selectedSetId = setId)
        }
    }
}