package com.keremmuhcu.flashcardsland.presentation.flashcards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily

@Composable
fun SearchBarComponent(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSearch:() -> Unit
) {
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = value,
        onValueChange = { onValueChange(it) },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontFamily = gintoFontFamily
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearch()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(percent = 50)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(Modifier.width(16.dp))
                Box {
                    innerTextField()
                    if (value.text.isEmpty()) {
                        BasicText(
                            text = "Terim ya da tanım ara...",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = gintoFontFamily,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                if (value.text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            onValueChange(TextFieldValue(""))
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }

        }
    )
}