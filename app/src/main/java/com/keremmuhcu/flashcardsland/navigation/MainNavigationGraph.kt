package com.keremmuhcu.flashcardsland.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardScreen
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardViewModel
import com.keremmuhcu.flashcardsland.presentation.flashcards.FlashcardsScreen
import com.keremmuhcu.flashcardsland.presentation.flashcards.FlashcardsViewModel
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListScreen
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.SetListScreenRoute
    ) {
        composable<Route.SetListScreenRoute> {
            val setListViewModel = koinViewModel<SetListViewModel>()
            val state = setListViewModel.state.collectAsStateWithLifecycle()

            SetListScreen(
                state = state,
                onEvent = setListViewModel::onEvent,
                navigateToAddOrEditFlashcardScreen = { setId->
                    navController.navigate(Route.AddOrEditFlashcardScreenRoute(setId))
                },
                navigateToFlashcardsScreen = { setId, setTitle ->
                    navController.navigate(Route.FlashcardsScreenRoute(setId, setTitle))
                }
            )
        }

        composable<Route.AddOrEditFlashcardScreenRoute> {
            val args = it.toRoute<Route.AddOrEditFlashcardScreenRoute>()
            val setId = args.setId
            val addOrEditFlashcardViewModel = koinViewModel<AddOrEditFlashcardViewModel>()
            val state = addOrEditFlashcardViewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                addOrEditFlashcardViewModel.setSelectedSetId(setId)
                args.flashcardId?.let { id->
                    addOrEditFlashcardViewModel.setSelectedFlashcard(id)
                }
            }
            AddOrEditFlashcardScreen(
                state = state,
                onEvent = addOrEditFlashcardViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.FlashcardsScreenRoute> {
            val args = it.toRoute<Route.FlashcardsScreenRoute>()

            val setId = args.setId
            val setTitle = args.setTitle

            val flashcardsViewModel = koinViewModel<FlashcardsViewModel>()
            val state = flashcardsViewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                flashcardsViewModel.loadData(setId = setId, setTitle = setTitle)
            }

            FlashcardsScreen(
                state = state.value,
                onEvent = flashcardsViewModel::onEvent,
                onNavigateBack = {
                    navController.navigateUp()
                },
                navigateToAddOrEditFlashcardScreen = { cardId->
                    cardId?.let {
                        navController.navigate(Route.AddOrEditFlashcardScreenRoute(setId, cardId))
                    } ?: navController.navigate(Route.AddOrEditFlashcardScreenRoute(setId))
                }
            )
        }
    }
}
