package com.keremmuhcu.flashcardsland.presentation.flashcards.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.domain.model.Flashcard
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

data class SegmentedButtonItem(
    val filter: String,
    val filteredList: List<Flashcard>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedButtonRowComponent(
    segmentedButtonsList: List<SegmentedButtonItem>,
    selectedSegmentButtonIndex: Int,
    onSegmentedButtonClicked: (Int) -> Unit,
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        SingleChoiceSegmentedButtonRow(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .horizontalScroll(
                rememberScrollState()
            )){
            segmentedButtonsList.forEachIndexed { index, button ->
                SegmentedButton(
                    selected = selectedSegmentButtonIndex == index,
                    onClick = {
                        onSegmentedButtonClicked(index)
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = segmentedButtonsList.size
                    )
                ) {
                    Text(text = "${button.filter}: ${button.filteredList.size}", fontFamily = gintoFontFamily)
                }
            }
        }
        content()
    }
}
