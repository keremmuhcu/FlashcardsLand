package com.keremmuhcu.flashcardsland.presentation.study.basic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.BasicStudyButtonSection
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.ExpandableExamplesTitleText
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.FlippableCard
import com.keremmuhcu.flashcardsland.presentation.study.components.StudyResults
import com.keremmuhcu.flashcardsland.presentation.study.components.StudyTopBar
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun BasicStudyScreen(
    basicStudyViewModel: BasicStudyViewModel = koinViewModel<BasicStudyViewModel>(),
    onNavigateBack: () -> Unit
) {
    val state by basicStudyViewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingComponent()
    } else {
        Scaffold(
            topBar = {
                StudyTopBar(
                    currentCardIndex = state.currentCardIndex,
                    flashcardCount = state.flashcards.size,
                    isFinish = state.isFinish,
                    currentCardIsHard = state.currentCardIsHard,
                    onFavoriteButtonClicked = {
                        basicStudyViewModel.onEvent(BasicStudyEvent.OnFavoriteButtonClicked)
                    },
                    onCloseIconClicked = {
                        onNavigateBack()
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (state.isFinish) {
                    true -> StudyResults(
                        wrongs = state.wrongs.toString(),
                        corrects = state.corrects.toString(),
                        remainingCards = state.remainingCards.toString(),
                        startNextRound = { basicStudyViewModel.onEvent(BasicStudyEvent.OnStartNextRoundClicked) },
                        repeatCurrentRound = { basicStudyViewModel.onEvent(BasicStudyEvent.OnRepeatCurrentRoundClicked) },
                        finishedButtonClicked = {
                            onNavigateBack()
                        }
                    )

                    false -> BasicStudyScreenContent(
                        state = state,
                        onEvent = basicStudyViewModel::onEvent
                    )
                }
            }
        }

    }

}

@Composable
private fun BasicStudyScreenContent(
    state: BasicStudyState,
    onEvent: (BasicStudyEvent) -> Unit,
) {
    var isExamplesExpanded by rememberSaveable { mutableStateOf(false) }
    val pagerState = rememberPagerState { state.flashcards.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onEvent(BasicStudyEvent.ChangeCurrentCardIndex(pagerState.currentPage))
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = 20.dp,
        pageSize = PageSize.Fill,
        userScrollEnabled = false,
        beyondViewportPageCount = 1,
        key = { it }
    ) { pageIndex ->
        Column(
            modifier = Modifier.fillMaxSize().graphicsLayer {
                val pageOffset = (
                        (pagerState.currentPage - pageIndex) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue

                lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
            },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FlippableCard(
                modifier = Modifier.weight(0.5f),
                englishWord = state.termSide,
                turkishWord = state.definitionSide
            )
            if (state.flashcards[state.currentCardIndex].examples[0].isNotBlank()) {
                ExpandableExamplesTitleText(
                    isExamplesExpanded = isExamplesExpanded,
                    onTitleClick = {
                        isExamplesExpanded = !isExamplesExpanded
                    }
                )

                AnimatedVisibility(visible = isExamplesExpanded, modifier = Modifier.weight(0.5f)) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(state.flashcards[state.currentCardIndex].examples) { index, example ->
                            Row {
                                Text(
                                    text = "${index + 1}. ",
                                    fontFamily = openSansFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(text = example, fontFamily = openSansFontFamily)
                            }
                        }
                    }
                }
            }
            if (!isExamplesExpanded) {
                Spacer(modifier = Modifier.height(50.dp))
            }
            BasicStudyButtonSection(
                onWrongButtonClick = {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1,
                                animationSpec = tween(
                                    durationMillis = 325,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                    }
                    isExamplesExpanded = false
                    onEvent(BasicStudyEvent.OnWrongButtonClicked)

                },
                onCorrectButtonClick = {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1,
                                animationSpec = tween(
                                    durationMillis = 325,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                    }
                    isExamplesExpanded = false
                    onEvent(BasicStudyEvent.OnCorrectButtonClicked)
                }
            )

        }
    }


}

@PreviewLightDark
@Composable
private fun ResultPreview() {
    FlashcardsLandTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StudyResults(
                    wrongs = "3",
                    corrects = "2",
                    remainingCards = "65",
                    startNextRound = {},
                    repeatCurrentRound = {},
                    finishedButtonClicked = {}
                )
            }

        }
    }
}