package com.keremmuhcu.flashcardsland.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object SetListScreenRoute

    @Serializable
    data class AddOrEditFlashcardScreenRoute(
        val setId: Int,
        val flashcardId: Int? = null
    )

    @Serializable
    data class FlashcardsScreenRoute(val setId: Int, val setTitle: String)

    @Serializable
    data class BasicStudyScreenRoute(val setId: Int)

    @Serializable
    data class MultipleAnswersScreenRoute(val setId: Int)
}
