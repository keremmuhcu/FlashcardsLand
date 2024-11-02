package com.keremmuhcu.flashcardsland.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.ui.theme.gintoFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Deneme(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(
                        targetState = isSearchActive,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(500)
                            ) togetherWith fadeOut(animationSpec = tween(500))
                        },
                        label = "Kerem",
                    ) {
                        if (transition.isRunning && !it) {
                            focusRequester.requestFocus()
                        }
                        when(it) {
                            true -> {
                                BasicTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequester),
                                    value = text,
                                    onValueChange = { text = it },
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp,
                                        fontFamily = gintoFontFamily
                                    ),
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
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
                                                if (text.isEmpty()) {
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
                                            if (text.isNotEmpty()) {
                                                Icon(
                                                    modifier = Modifier.clickable {
                                                        text = ""
                                                    },
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = null
                                                )
                                            }
                                        }

                                    }
                                )
                            }
                            false -> {
                                Text(text = "İngilizce", fontFamily = gintoFontFamily)

                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Geri tuşuna basıldığında yapılacak işlemler */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    AnimatedContent(
                        targetState = isSearchActive,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(500)
                            ) togetherWith fadeOut(animationSpec = tween(500))
                        },
                        label = "Kerem",
                    ) {
                        when(it) {
                            true -> {
                                TextButton(onClick = {
                                    isSearchActive = false
                                    text = ""
                                }) {
                                    Text(text = "İptal", fontFamily = gintoFontFamily, fontSize = 16.sp)
                                }
                            }
                            false -> {
                                IconButton(onClick = { isSearchActive = true }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                                }
                            }
                        }
                    }

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            TextField(
                value = "dngjad", onValueChange = {}, colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Button(onClick = { focusRequester.requestFocus() }) {
                Text(text = "Tıkla")
            }
        }
    }
}

@Preview
@Composable
private fun DePrev() {
    FlashcardsLandTheme {
        Deneme()
    }
}

/*SearchBar(
                                modifier = Modifier.weight(1f),
                                query = searchText,
                                onQueryChange = { searchText = it },
                                onSearch = { newQuery -> println(newQuery) },
                                active = false,
                                onActiveChange = {  },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                                },
                                trailingIcon = {
                                    if (searchText.isNotEmpty()) {
                                        Icon(
                                            modifier = Modifier
                                                .clickable {
                                                    searchText = ""
                                                },
                                            imageVector = Icons.Default.Close,
                                            contentDescription = ""
                                        )
                                    }
                                },
                                placeholder = {
                                    Text(text = "Terim ya da tanım ara", fontFamily = gintoFontFamily)
                                },
                                shape = SearchBarDefaults.inputFieldShape
                            ) {

                            }*/