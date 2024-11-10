package com.keremmuhcu.flashcardsland.presentation.flashcards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.domain.model.ListSortType
import com.keremmuhcu.flashcardsland.presentation.components.EmptyListScreenComponent
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.FlashcardsFilterDialog
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.SearchBarComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.SegmentedButtonItem
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.SegmentedButtonRowComponent
import com.keremmuhcu.flashcardsland.presentation.flashcards.components.flashcards
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import com.keremmuhcu.flashcardsland.util.toDate
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

    when(state.isLoading) {
        true -> LoadingComponent()
        else -> {
            Scaffold(
                topBar = {
                    FlashcardsTopAppBar(
                        state = state,
                        onEvent = flashcardsViewModel::onEvent,
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
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 20.dp
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
        val showDate = listOf(ListSortType.DATE_ASCENDING, ListSortType.DATE_DESCENDING)
        if (state.showDateSwitch && state.selectedSortType in showDate) {
            val groupedList = filteredFlashcards.groupBy { it.createdDate.toDate() }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                groupedList.forEach { (dateGroup, flashcardsList) ->
                    item {
                        Text(text = if (System.currentTimeMillis().toDate() == dateGroup) "Bugün" else dateGroup, fontFamily = openSansFontFamily)
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                    }

                    flashcards(
                        onCardClicked = { onCardClicked(it) },
                        showOnlyTerm = state.showOnlyTermRadioButton,
                        showOneSide = state.showOneSideSwitch,
                        isCardFlippable = state.canCardFlip,
                        onFavoriteButtonClicked = { flashcard ->
                            onEvent(FlashcardsEvent.ChangeFlashcardHardness(flashcard))
                        },
                        onDeleteClicked = { cardId->
                            onEvent(FlashcardsEvent.OnDeleteItemClicked(cardId))
                        },
                        flashcards = flashcardsList
                    )
                    item(span = StaggeredGridItemSpan.FullLine){

                    }

                }
            }

        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {

                flashcards(
                    onCardClicked = { onCardClicked(it) },
                    showOnlyTerm = state.showOnlyTermRadioButton,
                    showOneSide = state.showOneSideSwitch,
                    isCardFlippable = state.canCardFlip,
                    onFavoriteButtonClicked = { flashcard ->
                        onEvent(FlashcardsEvent.ChangeFlashcardHardness(flashcard))
                    },
                    onDeleteClicked = { cardId->
                        onEvent(FlashcardsEvent.OnDeleteItemClicked(cardId))
                    },
                    flashcards = filteredFlashcards
                )
            }
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlashcardsTopAppBar(
    state: FlashcardsState,
    onEvent: (FlashcardsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var isFilterDialogOpen by rememberSaveable { mutableStateOf(false) }
    TopAppBar(
        title = {
            AnimatedContent(
                targetState = state.searchBarActive,
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
                            value = state.searchBarTextTf,
                            onValueChange = { text-> onEvent(FlashcardsEvent.OnSearchBarTfChange(text)) },
                            onSearch = {
                                if (state.searchBarTextTf.text.isBlank()) {
                                    onEvent(FlashcardsEvent.OnSearchIconClicked(false))
                                    onEvent(FlashcardsEvent.OnSearchBarTfChange(TextFieldValue("")))
                                }
                            }
                        )
                    }
                    false -> Text(
                        text = state.selectedSetTitle,
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
            if (state.flashcards.isNotEmpty()) {
                AnimatedContent(
                    targetState = state.searchBarActive,
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
                                onEvent(FlashcardsEvent.OnSearchIconClicked(false))
                                onEvent(FlashcardsEvent.OnSearchBarTfChange(TextFieldValue("")))
                            }) {
                                Text(text = "İptal", fontFamily = openSansFontFamily, fontSize = 16.sp)
                            }
                        }
                        false -> {
                            Row {
                                IconButton(onClick = { onEvent(FlashcardsEvent.OnSearchIconClicked(true)) }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                                }

                                IconButton(onClick = { isFilterDialogOpen = true }) {
                                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.cards_settings), contentDescription = "")
                                }
                            }
                        }
                    }
                }
            }
        },
    )

    FlashcardsFilterDialog(
        isDialogOpen = isFilterDialogOpen,
        listSortType = state.selectedSortType,
        showDate = state.showDateSwitch,
        showOneSide = state.showOneSideSwitch,
        showOnlyTerm = state.showOnlyTermRadioButton,
        cardCanFlip = state.canCardFlip,
        confirmButtonClicked = {
            isFilterDialogOpen = false
            onEvent(FlashcardsEvent.OnConfirmFiltersButtonClicked)
        },
        onDismissRequest = {
            isFilterDialogOpen = false
            onEvent(FlashcardsEvent.OnDismissRequestClicked)
        },
        dropdownItemClicked = { onEvent(FlashcardsEvent.OnDropdownItemClicked(it)) },
        showDateSwitchChecked = { onEvent(FlashcardsEvent.OnShowDateSwitchClicked) },
        showOneSideSwitchChecked = { onEvent(FlashcardsEvent.OnShowOneSideSwitchClicked) },
        radioButtonClicked = { onEvent(FlashcardsEvent.OnRadioButtonClicked(it)) },
        canCardFlipCheckBoxClicked = { onEvent(FlashcardsEvent.OnCanCardFlipCheckBoxClicked) }
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