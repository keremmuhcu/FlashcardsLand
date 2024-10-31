package com.keremmuhcu.flashcardsland.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object SetListScreenRoute

    @Serializable
    data class AddOrEditFlashcardScreenRoute(val setId: Int? = null)
}