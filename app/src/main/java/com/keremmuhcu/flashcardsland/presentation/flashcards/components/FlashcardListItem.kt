package com.keremmuhcu.flashcardsland.presentation.flashcards.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.presentation.components.CustomAlertDialog
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily


fun LazyStaggeredGridScope.flashcards(
    onCardClicked: (Int) -> Unit,
    showOneSide: Boolean,
    isCardFlippable: Boolean,
    showOnlyTerm: Boolean,
    onFavoriteButtonClicked: (Flashcard) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    flashcards: List<Flashcard>,
) {

    itemsIndexed(flashcards) { index, card ->
        var recomp by remember { mutableIntStateOf(0) }
        LaunchedEffect(flashcards) {
            recomp++
        }
        if (showOneSide) {
            HideCardItem(
                card = card,
                isCardFlippable = isCardFlippable,
                showOnlyTerm = showOnlyTerm,
                onCardClicked = { onCardClicked(it) },
                onDeleteItemClicked = { onDeleteClicked(it) },
                onFavoriteButtonClicked = { onFavoriteButtonClicked(it) },
                recomp = recomp
            )
        } else {
            FlashcardListItem(
                onCardClicked = { onCardClicked(it) },
                onDeleteItemClicked = { onDeleteClicked(it) },
                onFavoriteButtonClicked = { onFavoriteButtonClicked(it) },
                card = card
            )
        }
    }

}

@Composable
private fun FlashcardListItem(
    onCardClicked: (Int) -> Unit,
    onDeleteItemClicked: (Int) -> Unit,
    onFavoriteButtonClicked: (Flashcard) -> Unit,
    card: Flashcard
) {
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
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
                    fontFamily = openSansFontFamily,
                    fontWeight = FontWeight.Bold,
                )
                Column {
                    Icon(
                        modifier = Modifier.clickable {
                            isDropdownExpanded = true
                        },
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ""
                    )
                    FlashcardDropdownOptions(
                        isExpanded = isDropdownExpanded,
                        onDismissAction = { isDropdownExpanded = false },
                        onEditItemClicked = {
                            isDropdownExpanded = false
                            onCardClicked(card.cardId!!)
                        },
                        onDeleteItemClicked = {
                            isDropdownExpanded = false
                            isDeleteDialogOpen = true
                        }
                    )
                }

            }
            Text(
                text = card.definition,
                fontFamily = openSansFontFamily,
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
                contentDescription = "",
                tint = if (card.isHard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    CustomAlertDialog(
        title = "Kart silinecek",
        text = "Bu işlem geri alınamaz",
        isOpen = isDeleteDialogOpen,
        onConfirm = {
            onDeleteItemClicked(card.cardId!!)
            isDeleteDialogOpen = false
        },
        onCancel = {
            isDeleteDialogOpen = false
        }
    )
}

@Composable
fun HideCardItem(
    card: Flashcard,
    isCardFlippable: Boolean,
    showOnlyTerm:Boolean,
    onCardClicked: (Int) -> Unit,
    onDeleteItemClicked: (Int) -> Unit,
    onFavoriteButtonClicked: (Flashcard) -> Unit,
    recomp: Int
) {
    var flipControl by rememberSaveable { mutableStateOf(false) }
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (flipControl) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )
    val isFrontVisible = rotationAngle <= 90f

    LaunchedEffect(isCardFlippable, showOnlyTerm, recomp) {
        flipControl = false
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotationAngle
                cameraDistance = 12 * density
                // Arka yüz için ters çevirme kontrolü
                if (rotationAngle > 90f) {
                    rotationY = 180f - (180f - rotationAngle)
                    scaleX = -1f  // Arka yüzü yatay olarak ters çevir
                }
            },
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { if(isCardFlippable) flipControl = !flipControl else onCardClicked(card.cardId!!) },
        colors = CardDefaults.cardColors(
            containerColor = if (flipControl)
                MaterialTheme.colorScheme.surfaceContainerLowest
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Column {

                    Icon(
                        modifier = Modifier.clickable {
                            isDropdownExpanded = true
                        },
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ""
                    )
                    FlashcardDropdownOptions(
                        isExpanded = isDropdownExpanded,
                        onDismissAction = { isDropdownExpanded = false },
                        onEditItemClicked = {
                            isDropdownExpanded = false
                            onCardClicked(card.cardId!!)
                        },
                        onDeleteItemClicked = {
                            isDropdownExpanded = false
                            isDeleteDialogOpen = true
                        }
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (isCardFlippable) {
                        if(showOnlyTerm) {
                            if (isFrontVisible) card.term else card.definition
                        } else {
                            if (isFrontVisible) card.definition else card.term
                        }
                    } else {
                        if (showOnlyTerm) card.term else card.definition
                    },
                    fontFamily = openSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            onFavoriteButtonClicked(card.copy(isHard = !card.isHard))
                        },
                    imageVector = if (card.isHard) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "",
                    tint = if (card.isHard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    CustomAlertDialog(
        title = "Kart silinecek",
        text = "Bu işlem geri alınamaz",
        isOpen = isDeleteDialogOpen,
        onConfirm = {
            onDeleteItemClicked(card.cardId!!)
            isDeleteDialogOpen = false
        },
        onCancel = {
            isDeleteDialogOpen = false
        }
    )
}

@Composable
private fun FlashcardDropdownOptions(
    isExpanded: Boolean,
    onDismissAction:() -> Unit,
    onEditItemClicked:() -> Unit,
    onDeleteItemClicked:() -> Unit
) {
    DropdownMenu(
        expanded = isExpanded, onDismissRequest = { onDismissAction() },
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Düzenle", fontFamily = openSansFontFamily)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
            },
            onClick = { onEditItemClicked() },

            )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(text = "Sil", fontFamily = openSansFontFamily, color = MaterialTheme.colorScheme.error)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = MaterialTheme.colorScheme.error)
            },
            onClick = { onDeleteItemClicked() }
        )
    }
}