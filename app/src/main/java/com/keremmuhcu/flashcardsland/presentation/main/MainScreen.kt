package com.keremmuhcu.flashcardsland.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.keremmuhcu.flashcardsland.navigation.MainNavigationGraph
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
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