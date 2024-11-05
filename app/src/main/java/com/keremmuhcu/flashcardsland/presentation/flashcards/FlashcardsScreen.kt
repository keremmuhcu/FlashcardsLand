package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.presentation.components.EmptyListScreenComponent
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.SegmentedButtonRowComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.SearchBarComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.SegmentedButtonItem
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.flashcards
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlashcardsScreen(
    flashcardsViewModel: FlashcardsViewModel = koinViewModel<FlashcardsViewModel>(),
    onNavigateBack: () -> Unit,
    navigateToAddOrEditFlashcardScreen: (Int?) -> Unit,
) {
    val state by flashcardsViewModel.state.collectAsStateWithLifecycle()
    val tabItems = listOf("Hepsi", "Zorlar")
    val isFabExpanded by remember {
        derivedStateOf {
            state.flashcards.size < 5 && !state.isLoading
        }
    }

    Scaffold(
        topBar = {
            FlashcardsTopAppBar(
                title = state.selectedSetTitle,
                isSearchActive = state.searchBarActive,
                onSearchBarActive = {
                    flashcardsViewModel.onEvent(FlashcardsEvent.OnSearchIconClicked(it))
                },
                searchBarValue = state.searchBarTextTf,
                onSearchBarTfChange = {
                    flashcardsViewModel.onEvent(FlashcardsEvent.OnSearchBarTfChange(it))
                },
                onSearch = {
                    if (state.searchBarTextTf.text.isBlank()) {
                        flashcardsViewModel.onEvent(FlashcardsEvent.OnSearchIconClicked(false))
                        flashcardsViewModel.onEvent(FlashcardsEvent.OnSearchBarTfChange(TextFieldValue("")))
                    }
                },
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToAddOrEditFlashcardScreen(null) },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Kart Ekle") },
                text = { Text(text = "Kart Ekle", fontFamily = openSansFontFamily)},
                expanded = isFabExpanded
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingComponent()
            state.flashcards.isEmpty() -> EmptyFlashcardsContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                onAddCard = { navigateToAddOrEditFlashcardScreen(null) }
            )

            else -> FlashcardsContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                state = state,
                tabItems = tabItems,
                onEvent = flashcardsViewModel::onEvent,
                onCardClicked = navigateToAddOrEditFlashcardScreen
            )
        }
    }
}


@Composable
private fun FlashcardsContent(
    state: FlashcardsState,
    tabItems: List<String>,
    onEvent: (FlashcardsEvent) -> Unit,
    onCardClicked: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { tabItems.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onEvent(FlashcardsEvent.OnTabSelected(pagerState.currentPage))
    }

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = state.selectedTabIndex) {
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    selected = state.selectedTabIndex == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        onEvent(FlashcardsEvent.OnTabSelected(index))
                    },
                    text = { Text(text = tab, fontFamily = openSansFontFamily) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            FlashcardsTabContent(
                state = state,
                tabIndex = index,
                onEvent = onEvent,
                onCardClicked = onCardClicked
            )
        }
    }
}

@Composable
private fun FlashcardsTabContent(
    state: FlashcardsState,
    tabIndex: Int,
    onEvent: (FlashcardsEvent) -> Unit,
    onCardClicked: (Int?) -> Unit
) {
    val filteredFlashcards = if (state.searchBarTextTf.text.isNotBlank()) {
        state.flashcards.filter {
            it.term.contains(state.searchBarTextTf.text, ignoreCase = true) ||
            it.definition.contains(state.searchBarTextTf.text, ignoreCase = true)
        }
    } else {
        getFilteredFlashcards(
            selectedSegmentButtonIndex = state.selectedSegmentButtonIndex,
            selectedSegmentButtonIndexHard = state.selectedSegmentButtonIndexHard,
            flashcards = state.flashcards,
            tabIndex = tabIndex
        )
    }

    val segmentedButtonsList = createSegmentedButtonsList(state.flashcards, tabIndex)

    SegmentedButtonRowComponent(
        onSegmentedButtonClicked = { buttonIndex ->
            if (tabIndex == 0) {
                onEvent(FlashcardsEvent.OnSegmentedButtonClicked(buttonIndex))
            } else {
                onEvent(FlashcardsEvent.OnSegmentedButtonClickedHard(buttonIndex))
            }
        },
        segmentedButtonsList = segmentedButtonsList,
        selectedSegmentButtonIndex = if (tabIndex == 0) {
            state.selectedSegmentButtonIndex
        } else {
            state.selectedSegmentButtonIndexHard
        }
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            flashcards(
                onCardClicked = { onCardClicked(it) },
                onFavoriteButtonClicked = { flashcard ->
                    onEvent(FlashcardsEvent.ChangeFlashcardHardness(flashcard))
                },
                flashcards = filteredFlashcards
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlashcardsTopAppBar(
    title: String,
    isSearchActive: Boolean,
    onSearchBarActive: (Boolean) -> Unit,
    searchBarValue: TextFieldValue,
    onSearchBarTfChange: (TextFieldValue) -> Unit,
    onSearch:() -> Unit,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(500)
                    ) togetherWith fadeOut(animationSpec = tween(500))
                },
                label = "Search Bar"
            ) {
                when(it) {
                    true -> {
                        SearchBarComponent(
                            value = searchBarValue,
                            onValueChange = { text-> onSearchBarTfChange(text) },
                            onSearch = onSearch
                        )
                    }
                    false -> Text(
                        text = title,
                        fontFamily = openSansFontFamily
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri"
                )
            }

        },
        actions = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(500)
                    ) togetherWith fadeOut(animationSpec = tween(500))
                },
                label = "Search Bar"
            ) {
                when(it) {
                    true -> {
                        TextButton(onClick = {
                            onSearchBarActive(false)
                            onSearchBarTfChange(TextFieldValue(""))
                        }) {
                            Text(text = "İptal", fontFamily = openSansFontFamily, fontSize = 16.sp)
                        }
                    }
                    false -> {
                        IconButton(onClick = { onSearchBarActive(true)}) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun EmptyFlashcardsContent(
    modifier: Modifier = Modifier,
    onAddCard: () -> Unit
) {
    EmptyListScreenComponent(
        modifier = modifier,
        onButtonClicked = onAddCard,
        infoIcon = R.drawable.no_cards,
        infoText = "Henüz kart eklemediniz.\nDaha verimli çalışmak için\nhemen başla.",
        buttonText = "Kart ekle"
    )
}


private fun getFilteredFlashcards(
    selectedSegmentButtonIndex: Int,
    selectedSegmentButtonIndexHard: Int,
    flashcards: List<Flashcard>,
    tabIndex: Int
): List<Flashcard> {
    return when (tabIndex) {
        0 -> when (selectedSegmentButtonIndex) {
            0 -> flashcards
            1 -> flashcards.filter { !it.isStudied }
            2 -> flashcards.filter { it.isStudied }
            else -> flashcards
        }

        1 -> when (selectedSegmentButtonIndexHard) {
            0 -> flashcards.filter { it.isHard }
            1 -> flashcards.filter { !it.isHardStudied && it.isHard }
            2 -> flashcards.filter { it.isHardStudied && it.isHard }
            else -> flashcards
        }

        else -> emptyList()
    }
}

private fun createSegmentedButtonsList(
    flashcards: List<Flashcard>,
    tabIndex: Int
): List<SegmentedButtonItem> = listOf(
    SegmentedButtonItem(
        "Tümü",
        if (tabIndex == 0) flashcards else flashcards.filter { it.isHard }
    ),
    SegmentedButtonItem(
        "Çalışılmayanlar",
        flashcards.filter { if (tabIndex == 1) !it.isHardStudied && it.isHard else !it.isStudied }
    ),
    SegmentedButtonItem(
        "Çalışılanlar",
        flashcards.filter { if (tabIndex == 1) it.isHardStudied && it.isHard else it.isStudied }
    )
)

/*@Composable
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToAddOrEditFlashcardScreen(null) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        when {
            state.isLoading -> LoadingComponent()
            state.flashcards.isEmpty() -> EmptyFlashcardsListScreen(
                modifier = modifier,
                onAddCard = { navigateToAddOrEditFlashcardScreen(null) }
            )
            else -> {
                val pagerState = rememberPagerState { tabItems.size }
                val scope = rememberCoroutineScope()

                LaunchedEffect(pagerState.currentPage) {
                    onEvent(FlashcardsEvent.OnTabSelected(pagerState.currentPage))
                    if (!pagerState.isScrollInProgress) {
                        onEvent(FlashcardsEvent.OnTabSelected(pagerState.currentPage))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    TabRow(selectedTabIndex = state.selectedTabIndex) {
                        tabItems.forEachIndexed { index, tab ->
                            Tab(
                                selected = state.selectedTabIndex == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                    onEvent(FlashcardsEvent.OnTabSelected(pagerState.currentPage))
                                },
                                text = { Text(text = tab , fontFamily = gintoFontFamily) }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { index ->
                        val filteredFlashcards = remember(
                            state.flashcards,
                            state.selectedTabIndex,
                            state.selectedSegmentButtonIndex,
                            state.selectedSegmentButtonIndexHard
                        ) {
                            when (state.selectedTabIndex) {
                                0 -> when (state.selectedSegmentButtonIndex) {
                                    0 -> state.flashcards
                                    1 -> state.flashcards.filter { !it.isStudied }
                                    2 -> state.flashcards.filter { it.isStudied }
                                    else -> state.flashcards
                                }
                                1 -> when (state.selectedSegmentButtonIndexHard) {
                                    0 -> state.flashcards.filter { it.isHard }
                                    1 -> state.flashcards.filter { !it.isHardStudied && it.isHard }
                                    2 -> state.flashcards.filter { it.isHardStudied && it.isHard }
                                    else -> state.flashcards
                                }
                                else -> emptyList()
                            }
                        }
                        val segmentedButtonsList = listOf(
                            SegmentedButtonItem("Tümü", if (index == 0) state.flashcards else state.flashcards.filter { it.isHard }),
                            SegmentedButtonItem("Çalışılmayanlar", state.flashcards.filter { if (index == 1) !it.isHardStudied && it.isHard else !it.isStudied} ),
                            SegmentedButtonItem("Çalışılanlar", state.flashcards.filter { if (index == 1) it.isHardStudied && it.isHard else it.isStudied} )
                        )
                        AllFlashcardsTabComponent(
                            onSegmentedButtonClicked = {
                                if (index == 0) {
                                    onEvent(FlashcardsEvent.OnSegmentedButtonClicked(it))
                                } else {
                                    onEvent(FlashcardsEvent.OnSegmentedButtonClickedHard(it))
                                }
                            },
                            segmentedButtonsList = segmentedButtonsList,
                            selectedSegmentButtonIndex = if (index == 0) {
                                state.selectedSegmentButtonIndex
                            } else {
                                state.selectedSegmentButtonIndexHard
                            },
                            content = {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    flashcards(
                                        onCardClicked = { cardId ->
                                            navigateToAddOrEditFlashcardScreen(cardId)
                                        },
                                        onFavoriteButtonClicked = {
                                            onEvent(FlashcardsEvent.ChangeFlashcardHardness(it))
                                        },
                                        flashcards = filteredFlashcards
                                    )
                                }
                            }
                        )
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

@Composable
private fun EmptyFlashcardsListScreen(
    modifier: Modifier = Modifier,
    onAddCard:() -> Unit
) {
    EmptyListScreenComponent(
        modifier = modifier,
        onButtonClicked = onAddCard,
        infoIcon = R.drawable.no_cards,
        infoText = "Henüz kart eklemediniz.\nDaha verimli çalışmak için\nhemen başla.",
        buttonText = "Kart ekle"
    )
}*/