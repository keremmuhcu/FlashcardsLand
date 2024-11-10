package com.keremmuhcu.flashcardsland.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.keremmuhcu.flashcardsland.navigation.MainNavigationGraph
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel<MainViewModel>()
) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    FlashcardsLandTheme(
        darkTheme = state.isDarkModeEnabled
    ) {
        val navController = rememberNavController()
        MainNavigationGraph(navController = navController, darkModeChange = {mainViewModel.darkModeChange()})
    }
}