package com.keremmuhcu.flashcardsland.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardScreen
import com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.AddOrEditFlashcardViewModel
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListScreen
import com.keremmuhcu.flashcardsland.presentation.set_list.SetListViewModel
import kotlinx.serialization.Serializable
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
                }
            )
        }
        composable<Route.AddOrEditFlashcardScreenRoute> {
            val args = it.toRoute<Route.AddOrEditFlashcardScreenRoute>()
            val setId = args.setId
            val addOrEditFlashcardViewModel = koinViewModel<AddOrEditFlashcardViewModel>()
            val state = addOrEditFlashcardViewModel.state.collectAsStateWithLifecycle()
            addOrEditFlashcardViewModel.setSelectedSetId(setId!!)

            AddOrEditFlashcardScreen(
                state = state,
                onEvent = addOrEditFlashcardViewModel::onEvent,
                onCloseButtonClicked = {
                    navController.popBackStack()
                }
            )
        }


    }
}
