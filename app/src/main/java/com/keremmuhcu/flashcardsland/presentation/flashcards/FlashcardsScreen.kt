package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.presentation.components.EmptyListScreenComponent
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.AllFlashcardsTabComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.flashcards
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily
import kotlinx.coroutines.launch

@Composable
fun FlashcardsScreen(
    state: FlashcardsState,
    onEvent: (FlashcardsEvent) -> Unit,
    onNavigateBack: () -> Unit,
    navigateToAddOrEditFlashcardScreen: (Int?) -> Unit,
) {
    val tabItems = listOf("Hepsi", "Zorlar")
    Scaffold(
        topBar = {
            FlashcardsTopAppBar(
                title = state.selectedSetTitle,
                onNavigateBack = { onNavigateBack() }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingComponent()
        } else {
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

            if(state.flashcards.isEmpty()) {
                EmptyListScreenComponent(
                    modifier = modifier,
                    onButtonClicked = { navigateToAddOrEditFlashcardScreen(null) },
                    infoIcon = R.drawable.no_cards,
                    infoText = "Henüz kart eklemediniz.\nDaha verimli çalışmak için\nhemen başla.",
                    buttonText = "Kart ekle"
                )
            } else {
                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val pagerState = rememberPagerState { tabItems.size }
                val scope = rememberCoroutineScope()

                LaunchedEffect(pagerState.currentPage) {
                    selectedTabIndex = pagerState.currentPage
                }
                LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
                    if (!pagerState.isScrollInProgress) {
                        selectedTabIndex = pagerState.currentPage
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    TabRow(selectedTabIndex = selectedTabIndex) {
                        tabItems.forEachIndexed { index, tab ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                    selectedTabIndex = index
                                },
                                text = { Text(text = tab, fontFamily = gintoFontFamily) }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { index ->
                        if (index == 0) {
                            AllFlashcardsTabComponent {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    flashcards(
                                        onCardClicked = { cardId ->
                                            navigateToAddOrEditFlashcardScreen(cardId)
                                        },
                                        flashcards = state.flashcards
                                    )
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlashcardsTopAppBar(
    title: String,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = gintoFontFamily
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
            }
        }
    )
}