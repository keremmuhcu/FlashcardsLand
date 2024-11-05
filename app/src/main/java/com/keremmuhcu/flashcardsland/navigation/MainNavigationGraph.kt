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
import com.keremmuhcu.flashcardsland.presentation.study.basic.BasicStudyScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.SetListScreenRoute
    ) {
        composable<Route.SetListScreenRoute> {
            SetListScreen(
                navigateToAddOrEditFlashcardScreen = { setId->
                    navController.navigate(Route.AddOrEditFlashcardScreenRoute(setId))
                },
                navigateToFlashcardsScreen = { setId, setTitle ->
                    navController.navigate(Route.FlashcardsScreenRoute(setId, setTitle))
                },
                navigateToBasicStudyScreen = { setId ->
                    navController.navigate(Route.BasicStudyScreenRoute(setId))
                }
            )

        }

        composable<Route.AddOrEditFlashcardScreenRoute> {
            AddOrEditFlashcardScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.FlashcardsScreenRoute> {
            val args = it.toRoute<Route.FlashcardsScreenRoute>()
            val setId = args.setId

            FlashcardsScreen(
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

        composable<Route.BasicStudyScreenRoute> {
            BasicStudyScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
