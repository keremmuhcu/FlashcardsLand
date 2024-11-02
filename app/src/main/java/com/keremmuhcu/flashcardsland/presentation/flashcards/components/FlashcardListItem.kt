package com.keremmuhcu.flashcardsland.presentation.flashcards.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily


fun LazyStaggeredGridScope.flashcards(
    onCardClicked: (Int) -> Unit,
    onFavoriteButtonClicked: (Flashcard) -> Unit,
    flashcards: List<Flashcard>,
) {
    itemsIndexed(flashcards) { index, card ->
        FlashcardListItem(
            onCardClicked = { onCardClicked(it) },
            onFavoriteButtonClicked = { onFavoriteButtonClicked(it) },
            card = card
        )
    }
}

@Composable
private fun FlashcardListItem(
    onCardClicked: (Int) -> Unit,
    onFavoriteButtonClicked: (Flashcard) -> Unit,
    card: Flashcard
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { onCardClicked(card.cardId!!) }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 6.dp, top = 6.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = card.term,
                    fontFamily = gintoFontFamily,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    modifier = Modifier.clickable {

                    },
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = ""
                )

            }
            Text(
                text = card.definition,
                fontFamily = gintoFontFamily,
                fontWeight = FontWeight.Light,
            )

            Icon(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 10.dp)
                    .align(Alignment.End)
                    .clickable {
                        onFavoriteButtonClicked(card.copy(isHard = !card.isHard))
                    },
                imageVector = if (card.isHard) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = ""
            )
        }
    }
}