package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
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
            AddOrEditFlashcardEvent.OnDeleteButtonClicked -> deleteFlashcard()
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

    /*private fun updateFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                flashcard = Flashcard(
                    cardId = cardId,
                    setId = state.value.selectedSetId,
                    term = state.value.termTf,
                    definition = state.value.definitionTf,
                    isHard = state.value.isHardSwitchChecked,
                    isStudied = false,
                    isHardStudied = false,
                    examples = if (state.value.isExampleSwitchChecked) state.value.examplesTfList else emptyList()
                )
            )
        }
    }*/

    private fun addAndGoNext() {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                flashcard = Flashcard(
                    setId = state.value.selectedSetId,
                    term = state.value.termTf.trim(),
                    definition = state.value.definitionTf.trim(),
                    isHard = state.value.isHardSwitchChecked,
                    isStudied = false,
                    isHardStudied = false,
                    examples =
                    if (state.value.isExampleSwitchChecked) {
                        state.value.examplesTfList.map { it.trim() }
                    } else emptyList()
                )
            )

            resetFields()
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
                isSuccessful = !it.isSuccessful
            )
        }
    }

    private fun addFlashcard() {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                flashcard = Flashcard(
                    cardId = state.value.selectedFlashcard?.cardId,
                    setId = state.value.selectedSetId,
                    term = state.value.termTf.trim(),
                    definition = state.value.definitionTf.trim(),
                    isHard = state.value.isHardSwitchChecked,
                    isStudied = false,
                    isHardStudied = false,
                    examples =
                        if (state.value.isExampleSwitchChecked) {
                            state.value.examplesTfList.map { it.trim() }
                        } else emptyList()
                )
            )
        }
    }

    fun deleteFlashcard() {
        viewModelScope.launch {
            flashcardRepository.deleteFlashcard(state.value.selectedFlashcard!!)
        }
    }

    fun setSelectedSetId(setId: Int) {
        _state.update {
            it.copy(selectedSetId = setId)
        }
    }

    fun setSelectedFlashcard(flashcardId: Int) {
        viewModelScope.launch {
            val flashcard = flashcardRepository.getFlashcardById(flashcardId)
            println(flashcard.examples.size)
            _state.update {
                it.copy(
                    selectedFlashcard = flashcard,
                    termTf = flashcard.term,
                    definitionTf = flashcard.definition,
                    isHardSwitchChecked = flashcard.isHard,
                    isExampleSwitchChecked = !(flashcard.examples.size == 1 && flashcard.examples[0].isEmpty()),
                    examplesTfList = flashcard.examples
                )
            }
        }
    }

}