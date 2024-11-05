package com.keremmuhcu.flashcardsland.presentation.set_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun AddOrEditSetDialog(
    isOpen: Boolean,
    label: String,
    setTitle: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var setTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    var textFieldLoaded by remember { mutableStateOf(false) }


    setTitleError = when {
        setTitle.text.isNotEmpty() && setTitle.text.isBlank() -> "Set adı boş olamaz."
        setTitle.text.length < 2 -> "Set adı çok kısa."
        setTitle.text.length > 45 -> "Set adı çok uzun."
        else -> null
    }

    if (isOpen) {
        AlertDialog(
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = label,
                        fontFamily = openSansFontFamily,
                        fontWeight = FontWeight.Medium,
                    )
                    TextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if(!textFieldLoaded) {
                                    textFieldLoaded = true
                                    focusRequester.requestFocus()
                                }
                            }
                        ,
                        value = setTitle,
                        onValueChange = {
                            onTitleChange(it)
                        },
                        label = {
                            Text(
                                text = "Set Adı",
                                fontFamily = openSansFontFamily,
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = openSansFontFamily,
                        ),
                        isError = setTitleError != null && setTitle.text.isNotEmpty(),
                        supportingText = {
                            Text(
                                text = if (setTitle.text.isEmpty()) "Set adı boş olamaz." else setTitleError ?: "",
                                fontFamily = openSansFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.Sentences),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        )
                    )
                }
            },
            onDismissRequest = {  },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = { onCancel() }) {
                        Text(text = "İptal", fontFamily = openSansFontFamily)
                    }

                    Button(
                        onClick = { onConfirm() },
                        enabled = setTitleError == null
                    ) {
                        Text(text = "Tamam", fontFamily = openSansFontFamily)
                    }

                }
            }
        )


    }
}