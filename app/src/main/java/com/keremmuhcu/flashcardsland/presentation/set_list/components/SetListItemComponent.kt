package com.keremmuhcu.flashcardsland.presentation.set_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.domain.model.FlashcardSetWithCards
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@Composable
fun SetListItemComponent(
    content: @Composable () -> Unit = {},
    setItemClicked:() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        onClick = {setItemClicked() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            content()
        }
    }
}

@Composable
fun SetListCardItemContentComponent(
    set: FlashcardSetWithCards,
    isDropdownMenuOpen: Boolean,
    onDropdownClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    val subText = if (set.cards.isNotEmpty()) {
        val studiedCount = set.cards.filter { it.isStudied }.size
        "$studiedCount/${set.cards.size}"
    } else {
        "Kart ekleyiniz"
    }
    Column{
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = set.flashcardSet.title,
                fontFamily = gintoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            IconButton(onClick = { onDropdownClicked() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "setMoreActions"
                )
                SetListCardItemOptionsDropdownComponent(
                    isExpanded = isDropdownMenuOpen,
                    onDismissAction = { onDropdownClicked() },
                    onEditItemClicked = {
                        onEditClicked()
                    },
                    onDeleteItemClicked = {
                        onDeleteClicked()
                    }
                )
            }

        }
        Text(
            text = subText,
            fontFamily = gintoFontFamily,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun SetListCardItemButtonsComponent(
    isStudyButtonEnabled: Boolean,
    studyButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedButton(
            onClick = { studyButtonClicked() },
            modifier = Modifier
                .height(35.dp)
                .width(75.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Ekle",
                fontFamily = gintoFontFamily,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = {  },
            modifier = Modifier
                .height(35.dp)
                .width(75.dp),
            contentPadding = PaddingValues(0.dp),
            enabled = isStudyButtonEnabled
        ) {
            Text(
                text = "Çalış",
                fontFamily = gintoFontFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun SetListItemPrev() {
    FlashcardsLandTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                //SetListItemComponent()
            }
        }
    }
}