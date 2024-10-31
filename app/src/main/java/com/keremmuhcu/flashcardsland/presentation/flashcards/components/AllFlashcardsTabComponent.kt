package com.keremmuhcu.flashcardsland.presentation.flashcards.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFlashcardsTabComponent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val segmentliButtonlar = listOf("Tümü", "Çalışılanlar", "Çalışılmayanlar")
    var selectedSegmentButtonIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        SingleChoiceSegmentedButtonRow(modifier = Modifier
            .padding(bottom = 4.dp)
            .horizontalScroll(
                rememberScrollState()
            )){
            segmentliButtonlar.forEachIndexed { index, buton ->
                SegmentedButton(
                    selected = selectedSegmentButtonIndex == index,
                    onClick = { selectedSegmentButtonIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = segmentliButtonlar.size
                    )
                ) {
                    Text(text = buton, fontFamily = gintoFontFamily)
                }
            }
        }
        content()
    }
}

fun LazyStaggeredGridScope.flashcards(
    onCardClicked:(Int) -> Unit,
    flashcards: List<Flashcard>,
) {
    itemsIndexed(flashcards) { index, card ->
        Card(
            elevation = CardDefaults.cardElevation(2.dp),
            onClick = {onCardClicked(card.cardId!!)}
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

                        },
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = ""
                )
            }
        }
    }
}