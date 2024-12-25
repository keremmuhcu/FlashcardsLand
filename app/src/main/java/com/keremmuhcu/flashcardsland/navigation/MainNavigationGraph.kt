package com.keremmuhcu.flashcardsland.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardScreen
import com.keremmuhcu.flashcardsland.presentation.flashcards.FlashcardsScreen
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListScreen
import com.keremmuhcu.flashcardsland.presentation.study.basic.BasicStudyScreen
import com.keremmuhcu.flashcardsland.presentation.study.multiple_answers.MultipleAnswersStudyScreen


@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    darkModeChange: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Route.SetListScreenRoute,
        /*enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }*/
    ) {
        composable<Route.SetListScreenRoute>(
            exitTransition = { slideOutHorizontally() },
            popEnterTransition = { slideInHorizontally() }
        ){
            SetListScreen(
                navigateToAddOrEditFlashcardScreen = { setId->
                    navController.navigate(Route.AddOrEditFlashcardScreenRoute(setId)) {
                        launchSingleTop = true
                    }
                },
                navigateToFlashcardsScreen = { setId, setTitle ->
                    navController.navigate(Route.FlashcardsScreenRoute(setId, setTitle)) {
                        launchSingleTop = true
                    }
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

        composable<Route.AddOrEditFlashcardScreenRoute>(
            enterTransition = { slideInHorizontally { initialOffset ->
                initialOffset
            } },
            exitTransition = { slideOutHorizontally { initialOffset ->
                initialOffset
            } },
            popEnterTransition = { slideInHorizontally() }
        ) {
            AddOrEditFlashcardScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.FlashcardsScreenRoute>(
            enterTransition = { slideInHorizontally { initialOffset ->
                initialOffset
            } },
            exitTransition = {
                if (navController.currentDestination?.route?.contains("AddOrEditFlashcardScreenRoute") == true) {
                    slideOutHorizontally()
                } else {
                    slideOutHorizontally { initialOffset ->
                        initialOffset
                    }
                }

            },
            popEnterTransition = { slideInHorizontally() }
        ){
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

        composable<Route.BasicStudyScreenRoute>(
            enterTransition = { slideInHorizontally { initialOffset ->
                initialOffset
            } },
            exitTransition = { slideOutHorizontally { initialOffset ->
                initialOffset
            } },
            popEnterTransition = { slideInHorizontally() }
        ) {
            BasicStudyScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.MultipleAnswersScreenRoute>(
            enterTransition = { slideInHorizontally { initialOffset ->
                initialOffset
            } },
            exitTransition = { slideOutHorizontally { initialOffset ->
                initialOffset
            } },
            popEnterTransition = { slideInHorizontally() }
        ) {
            MultipleAnswersStudyScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
