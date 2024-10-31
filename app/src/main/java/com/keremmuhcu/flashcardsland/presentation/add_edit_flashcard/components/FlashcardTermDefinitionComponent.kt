package com.keremmuhcu.flashcardsland.presentation.add_edit_flashcard.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@Composable
fun FlashcardTermDefinitionComponent(
    modifier: Modifier,
    focusTermTextField: Boolean,
    termTextField: String,
    onTermTextFieldChange: (String) -> Unit,
    definitionTextField: String,
    onDefinitionTextFieldChange: (String) -> Unit,
    tfTextStyle: TextStyle
) {
    var termTfError by rememberSaveable { mutableStateOf<String?>(null) }
    var definitionTfError by rememberSaveable { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusTermTextField) {
        focusRequester.requestFocus()
    }

    termTfError = when {
        termTextField.isNotEmpty() && termTextField.isBlank() -> "Terim boş olamaz"
        else -> null
    }

    definitionTfError = when {
        definitionTextField.isNotEmpty() && definitionTextField.isBlank() -> "Tanım boş olamaz."
        else -> null
    }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = termTextField,
        onValueChange = { onTermTextFieldChange(it) },
        label = {
            Text(
                text = "Terim",
                fontFamily = gintoFontFamily,
            )
        },
        textStyle = tfTextStyle,
        isError = termTfError != null,
        supportingText = {
            Text(
                text = if (termTfError != null || termTextField.isEmpty()) "Terim boş olamaz." else "",
                fontFamily = gintoFontFamily,
                fontWeight = FontWeight.Light
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next,

        ),
        keyboardActions = KeyboardActions(
            onNext = {
                defaultKeyboardAction(ImeAction.Next)
            }
        ),
    )
    
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        modifier = modifier, // fillmaxWidth
        value = definitionTextField,
        onValueChange = { onDefinitionTextFieldChange(it) },
        label = {
            Text(
                text = "Tanım",
                fontFamily = gintoFontFamily,
            )
        },
        textStyle = tfTextStyle,
        isError = definitionTfError != null,
        supportingText = {
            Text(
                text = if (definitionTfError != null || definitionTextField.isEmpty()) "Tanım boş olamaz." else "",
                fontFamily = gintoFontFamily,
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                defaultKeyboardAction(ImeAction.Done)
            }
        )
    )
}