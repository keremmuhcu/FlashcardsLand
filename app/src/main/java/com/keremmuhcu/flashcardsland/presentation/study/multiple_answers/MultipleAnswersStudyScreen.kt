package com.keremmuhcu.flashcardsland.presentation.study.multiple_answers

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.study.components.StudyResults
import com.keremmuhcu.flashcardsland.presentation.study.components.StudyTopBar
import com.keremmuhcu.flashcardsland.presentation.study.multiple_answers.components.OptionCard
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MultipleAnswersStudyScreen(
    multipleAnswersStudyViewModel: MultipleAnswersStudyViewModel = koinViewModel<MultipleAnswersStudyViewModel>(),
    onNavigateBack: () -> Unit,
) {
    val state by multipleAnswersStudyViewModel.state.collectAsStateWithLifecycle()
    when(state.isLoading) {
        true -> LoadingComponent()
        false -> {
            Scaffold(
                topBar = {
                    StudyTopBar(
                        currentCardIsHard = state.currentCardIsHard,
                        currentCardIndex = state.currentCardIndex,
                        flashcardCount = state.flashcards.size,
                        isFinish = state.isFinish,
                        onCloseIconClicked = onNavigateBack,
                        onFavoriteButtonClicked = {
                            multipleAnswersStudyViewModel.onEvent(
                                MultipleAnswersStudyEvent.OnFavoriteButtonClicked
                            )
                        }
                    )
                }
            ) { innerPadding ->
                val modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)

                when(state.isFinish) {
                    true -> StudyResults(
                        modifier = modifier,
                        wrongs = state.wrongs.toString(),
                        corrects = state.corrects.toString(),
                        remainingCards = state.remainingCards.toString(),
                        startNextRound = { multipleAnswersStudyViewModel.onEvent(MultipleAnswersStudyEvent.OnStartNextRoundClicked) },
                        repeatCurrentRound = { multipleAnswersStudyViewModel.onEvent(MultipleAnswersStudyEvent.OnRepeatCurrentRoundClicked) },
                        finishedButtonClicked = {
                            onNavigateBack()
                        }
                    )
                    else ->MultipleAnswersStudyScreenContent(
                        modifier = modifier,
                        state = state,
                        onEvent = multipleAnswersStudyViewModel::onEvent,
                    )
                }

            }
        }
    }
}


@Composable
fun MultipleAnswersStudyScreenContent(
    modifier: Modifier = Modifier,
    state: MultipleAnswersStudyState,
    onEvent: (MultipleAnswersStudyEvent) -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Text(
            text = state.question,
            fontFamily = openSansFontFamily,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.height(28.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(state.options) { answer ->
                OptionCard(
                    answer = answer,
                    isSelected = selected.isNotEmpty(),
                    isCorrect = answer == if (state.settings.workDefinitions) state.flashcards[state.currentCardIndex].term else state.flashcards[state.currentCardIndex].definition,
                    isSelectedWrong = selected == answer && !isCorrectAnswer(state, answer),
                    onClick = {
                        selected = answer
                        onAnswerSelected(scope, answer, state, onEvent, resetSelected = {selected = ""})
                    }
                )
            }
        }
    }
}

@Composable
fun getContainerColor(isSelected: Boolean, isCorrect: Boolean, isSelectedWrong: Boolean): Color {
    return when {
        !isSelected -> Color.Transparent
        isCorrect -> MaterialTheme.colorScheme.primaryContainer
        isSelectedWrong -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Transparent
    }
}

fun onAnswerSelected(
    scope: CoroutineScope,
    selectedAnswer: String,
    state: MultipleAnswersStudyState,
    onEvent: (MultipleAnswersStudyEvent) -> Unit,
    resetSelected: () -> Unit
) {
    scope.launch {
        val isCorrect = selectedAnswer == if (state.settings.workDefinitions) state.flashcards[state.currentCardIndex].term else state.flashcards[state.currentCardIndex].definition
        delay(if (isCorrect) 1000L else 2000L)
        onEvent(MultipleAnswersStudyEvent.OnOptionClicked(isCorrect))
        //onEvent(MultipleAnswersStudyEvent.ChangeCurrentCardIndex(state.currentCardIndex + 1))
        resetSelected()
    }
}

fun isCorrectAnswer(state: MultipleAnswersStudyState, answer: String): Boolean {
    return answer == if (state.settings.workDefinitions) state.flashcards[state.currentCardIndex].term else state.flashcards[state.currentCardIndex].definition
}


