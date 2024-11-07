package com.keremmuhcu.flashcardsland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.keremmuhcu.flashcardsland.navigation.MainNavigationGraph
import com.keremmuhcu.flashcardsland.presentation.main.MainScreen
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.util.Deneme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
            /*FlashcardsLandTheme {
                Deneme()
            }*/
        }
    }
}

