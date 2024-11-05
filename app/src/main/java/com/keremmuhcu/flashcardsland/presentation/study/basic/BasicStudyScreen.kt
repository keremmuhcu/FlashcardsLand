package com.keremmuhcu.flashcardsland.presentation.study.basic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.presentation.components.CustomAlertDialog
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.BasicStudyButtonSection
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.ExpandableExamplesTitleText
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.FlippableCard
import com.keremmuhcu.flashcardsland.presentation.study.basic.components.ResultTextRow
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
                BasicStudyScreenTopBar(
                    currentCardIndex = state.currentCardIndex,
                    flashcardCount = state.flashcards.size,
                    isFinish = state.isFinish,
                    currentCardIsHard = if (!state.isFinish) state.flashcards[state.currentCardIndex].isHard else false,
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
private fun StudyResults(
    wrongs: String,
    corrects: String,
    remainingCards: String,
    startNextRound: () -> Unit,
    repeatCurrentRound: () -> Unit,
    finishedButtonClicked:() -> Unit
) {
    val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebration_lottie))
    val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.successful_lottie))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.Center
        ) {
            if (wrongs.toInt() == 0) LottieAnimation(composition = composition1)
            LottieAnimation(composition = composition2)
        }
        Text(
            text = "Çalışma tamamlandı!",
            fontFamily = openSansFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        HorizontalDivider()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
        ) {
            Column(
                modifier = Modifier.padding(all = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResultTextRow(
                    text = if (wrongs.toInt() == 0) "Tebrikler! Hepsi Doğru: " else "Doğrular: ",
                    result = corrects,
                    color = MaterialTheme.colorScheme.primary
                )
                if (wrongs.toInt() > 0) {
                    ResultTextRow(
                        text = "Yanlışlar: ",
                        result = wrongs,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                ResultTextRow(
                    text = "Kalan kartlar: ",
                    result = remainingCards,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Spacer(
            modifier = Modifier.weight(0.8f)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { startNextRound() }
                ) {
                    Text(text = "SONRAKİ TUR", fontFamily = openSansFontFamily)
                }
                OutlinedButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { repeatCurrentRound() }
                ) {
                    Text(text = "TURU TEKRARLA", fontFamily = openSansFontFamily)
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { finishedButtonClicked() }
            ) {
                Text(text = "BİTİR", fontFamily = openSansFontFamily)
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
                englishWord = state.flashcards[state.currentCardIndex].term,
                turkishWord = state.flashcards[state.currentCardIndex].definition
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
                    onEvent(BasicStudyEvent.OnCorrectButtonClicked)
                }
            )

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicStudyScreenTopBar(
    currentCardIsHard: Boolean,
    currentCardIndex: Int,
    flashcardCount: Int,
    isFinish: Boolean,
    onCloseIconClicked: () -> Unit
) {
    var goBackAlertDialogControl by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (isFinish) {
                    ""
                } else {
                    "${currentCardIndex + 1}/$flashcardCount"
                },
                fontFamily = openSansFontFamily
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (isFinish) {
                    onCloseIconClicked()
                } else {
                    goBackAlertDialogControl = true
                }
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        },
        actions = {
            if (!isFinish) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = if (currentCardIsHard) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "",
                        tint = if (currentCardIsHard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )

    CustomAlertDialog(
        title = "Çalışma sonlandırılacak",
        text = "Şimdiye kadarki olan ilerlemeniz kaydedilecek.",
        isOpen = goBackAlertDialogControl,
        onConfirm = {
            goBackAlertDialogControl = false
            onCloseIconClicked()
        },
        onCancel = {
            goBackAlertDialogControl = false
        }
    )
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