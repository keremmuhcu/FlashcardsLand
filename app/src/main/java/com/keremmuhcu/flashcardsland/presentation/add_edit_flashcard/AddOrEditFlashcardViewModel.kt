package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardRepository
import com.keremmuhcu.flashcardsland.domain.repository.FlashcardSetRepository
import com.keremmuhcu.flashcardsland.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddOrEditFlashcardViewModel(
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val setId = savedStateHandle.toRoute<Route.AddOrEditFlashcardScreenRoute>().setId
    private val cardId = savedStateHandle.toRoute<Route.AddOrEditFlashcardScreenRoute>().flashcardId

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
                _state.update { it.copy(isExampleSwitchChecked = !it.isExampleSwitchChecked) }
            }
            AddOrEditFlashcardEvent.OnHardSwitchChange -> {
                _state.update { it.copy(isHardSwitchChecked = !it.isHardSwitchChecked) }
            }
            AddOrEditFlashcardEvent.OnSaveButtonClicked -> addFlashcard()
            AddOrEditFlashcardEvent.OnAddExampleIconClicked -> {
                _state.update { it.copy(examplesTfList = it.examplesTfList + TextFieldValue("")) }
            }
            is AddOrEditFlashcardEvent.OnDefinitionTextFieldChange -> {
                _state.update { it.copy(definitionTf = event.definition) }
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

    init {
        _state.update {
            it.copy(selectedSetId = setId)
        }
        if (cardId != null) {
            viewModelScope.launch {
                val flashcard = flashcardRepository.getFlashcardById(cardId)
                _state.update {
                    it.copy(
                        selectedFlashcard = flashcard,
                        termTf = TextFieldValue(flashcard.term, TextRange(flashcard.term.length)),
                        definitionTf = TextFieldValue(flashcard.definition, TextRange(flashcard.definition.length)),
                        isHardSwitchChecked = flashcard.isHard,
                        isExampleSwitchChecked = !(flashcard.examples.size == 1 && flashcard.examples[0].isEmpty()),
                        examplesTfList = flashcard.examples.map {tf-> TextFieldValue(tf, TextRange(tf.length)) },
                    )
                }
            }
        }
    }


    private fun addAndGoNext() {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                flashcard = Flashcard(
                    setId = state.value.selectedSetId,
                    term = state.value.termTf.text.trim(),
                    definition = state.value.definitionTf.text.trim(),
                    isHard = state.value.isHardSwitchChecked,
                    isStudied = false,
                    isHardStudied = false,
                    examples =
                    if (state.value.isExampleSwitchChecked) {
                        state.value.examplesTfList.map { it.text.trim() }
                    } else emptyList(),
                    createdDate = state.value.selectedFlashcard?.createdDate ?: System.currentTimeMillis()
                )
            )
            // only difference between this method and addFlashcard method:
            resetFields()
        }
    }

    private fun addFlashcard() {
        viewModelScope.launch {
            flashcardRepository.upsertFlashcard(
                flashcard = Flashcard(
                    cardId = state.value.selectedFlashcard?.cardId,
                    setId = state.value.selectedSetId,
                    term = state.value.termTf.text.trim(),
                    definition = state.value.definitionTf.text.trim(),
                    isHard = state.value.isHardSwitchChecked,
                    isStudied = false,
                    isHardStudied = false,
                    examples =
                    if (state.value.isExampleSwitchChecked) {
                        state.value.examplesTfList.map { it.text.trim() }
                    } else emptyList(),
                    createdDate = state.value.selectedFlashcard?.createdDate ?: System.currentTimeMillis()
                )
            )
        }
    }

    private fun resetFields() {
        _state.update {
            it.copy(
                termTf = TextFieldValue(""),
                definitionTf = TextFieldValue(""),
                isHardSwitchChecked = false,
                isExampleSwitchChecked = false,
                examplesTfList = listOf(TextFieldValue("")),
                isSuccessful = !it.isSuccessful
            )
        }
    }

    private fun deleteFlashcard() {
        viewModelScope.launch {
            flashcardRepository.deleteFlashcard(state.value.selectedFlashcard!!)
        }
    }

}