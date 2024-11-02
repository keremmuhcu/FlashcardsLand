package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun LazyListScope.flashcardExamplesList(
    examplesTextFields: List<TextFieldValue>,
    listSize: Int,
    onExampleTextFieldChange: (index: Int, value: TextFieldValue) -> Unit,
    removeExampleIconClicked: (index: Int) -> Unit,
    addExampleIconClicked: () -> Unit
) {

    itemsIndexed(examplesTextFields) { index, exampleTf ->
        ExampleListItem(
            index = index,
            listSize = listSize,
            exampleTf = exampleTf,
            onExampleTextFieldChange = onExampleTextFieldChange,
            removeExampleIconClicked = removeExampleIconClicked,
            addExampleIconClicked = addExampleIconClicked,
        )
    }

    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        addExampleIconClicked()
                    },
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

}

@Composable
private fun ExampleListItem(
    index: Int,
    listSize: Int,
    exampleTf: TextFieldValue,
    onExampleTextFieldChange: (index: Int, value: TextFieldValue) -> Unit,
    removeExampleIconClicked: (index: Int) -> Unit,
    addExampleIconClicked: () -> Unit,
) {
    val exampleTfError = when {
        exampleTf.text.isNotEmpty() && exampleTf.text.isBlank() -> "Örnek boş olamaz."
        else -> null
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scope = rememberCoroutineScope()
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                ,//.focusRequester(if (index == 0) focusRequester else FocusRequester()),
            value = exampleTf,
            onValueChange = {
                onExampleTextFieldChange(index, it)
            },
            label = {
                Text(
                    text = "Örnek ${index + 1}",
                    fontFamily = gintoFontFamily,
                    //fontSize = 16.sp
                )
            },
            textStyle = TextStyle(
                fontFamily = gintoFontFamily,
                fontSize = 16.sp
            ),
            isError = exampleTfError != null,
            supportingText = {
                Text(
                    text = if (exampleTfError != null || exampleTf.text.isEmpty()) "Örnek boş olamaz." else "",
                    fontFamily = gintoFontFamily
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = if (listSize == index + 1) ImeAction.Go else ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    //focusManager.moveFocus(FocusDirection.Down)
                    defaultKeyboardAction(ImeAction.Next)
                },
                onGo = {
                    scope.launch{
                        addExampleIconClicked()
                        delay(50L)
                        defaultKeyboardAction(ImeAction.Next)
                    }
                }
            )
        )

        if (listSize > 1) {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(28.dp)
                    .clickable {
                        removeExampleIconClicked(index)
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.remove_example),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }

}