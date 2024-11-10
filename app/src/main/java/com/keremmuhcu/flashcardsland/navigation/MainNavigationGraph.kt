package com.keremmuhcu.flashcardsland.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardScreen
import com.keremmuhcu.flashcardsland.presentation.flashcards.FlashcardsScreen
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListScreen
import com.keremmuhcu.flashcardsland.presentation.study.basic.BasicStudyScreen
import com.keremmuhcu.flashcardsland.presentation.study.multiple_answers.MultipleAnswersStudyScreen
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme


@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    darkModeChange: () -> Unit,
) {
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
                },
                navigateToMultipleAnswersStudyScreen = { setId ->
                    navController.navigate(Route.MultipleAnswersScreenRoute(setId))
                },
                toggleDarkMode = darkModeChange
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

        composable<Route.MultipleAnswersScreenRoute> {
            MultipleAnswersStudyScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
