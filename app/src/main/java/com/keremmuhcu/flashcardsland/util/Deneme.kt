package com.keremmuhcu.flashcardsland.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DialogViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DialogUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: DenemeEvent) {
        when(event) {
            DenemeEvent.OnButtonClicked -> {
                _uiState.update {
                    it.copy(isDialogVisible = true)
                }
            }
            is DenemeEvent.ChangeDialogVisibility -> {
                _uiState.update {
                    it.copy(isDialogVisible = event.visible)
                }
            }

            is DenemeEvent.OnTextChange -> {
                _uiState.update {
                    it.copy(text = event.text)
                }
            }
        }
    }

}

sealed class DenemeEvent {
    data object OnButtonClicked: DenemeEvent()
    data class ChangeDialogVisibility(val visible: Boolean): DenemeEvent()
    data class OnTextChange(val text: String): DenemeEvent()
}

data class DialogUiState(
    val isDialogVisible: Boolean = false,
    val text: String = ""
)

@Composable
fun Deneme(
    viewModel: DialogViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {onEvent(DenemeEvent.OnButtonClicked)}) {
            Text("Dialog'u Aç")
        }
    }

    if (uiState.isDialogVisible) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Dialog(
            onDismissRequest = {onEvent(DenemeEvent.ChangeDialogVisibility(false))},
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Dialog Başlığı",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.text,
                        onValueChange = {onEvent(DenemeEvent.OnTextChange(it))},
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                focusRequester.requestFocus()
                            }
                            .focusRequester(focusRequester),

                        label = { Text("Metin giriniz") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {onEvent(DenemeEvent.ChangeDialogVisibility(false))}) {
                            Text("İptal")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {}) {
                            Text("Tamam")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DePrev() {
    FlashcardsLandTheme {
        //Deneme()
    }
}
